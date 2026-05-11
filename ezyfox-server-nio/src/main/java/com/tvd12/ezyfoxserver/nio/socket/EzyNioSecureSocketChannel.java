package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzySecureChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

public class EzyNioSecureSocketChannel
    extends EzyNioSocketChannel
    implements EzySecureChannel {

    /*
     * Buffer state convention:
     * - netBuffer stores encrypted inbound TLS bytes and is kept in write mode
     *   between read calls. read() flips it before unwrap and compacts it before
     *   returning, so partial TLS records remain available for the next read.
     * - appBuffer stores plaintext produced by SSLEngine.unwrap(). It is reused
     *   to reduce per-read allocations and is drained into the returned byte[].
     * - outboundNetBuffer stores encrypted outbound TLS bytes produced by
     *   SSLEngine.wrap(). It is reused while pack() may emit multiple TLS
     *   records for one application payload.
     */
    private SSLEngine engine;
    private ByteBuffer netBuffer;
    private ByteBuffer appBuffer;
    private ByteBuffer outboundNetBuffer;
    private int appBufferSize;
    private int netBufferSize;
    private int sslMaxNetBufferSize;
    private volatile SSLContext sslContext;
    private final int sslHandshakeTimeout;
    private final int sslMaxAppBufferSize;
    @Getter
    private final Object packingLock = new Object();
    private final AtomicBoolean handshakeComplete = new AtomicBoolean();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);

    public EzyNioSecureSocketChannel(
        SocketChannel channel,
        EzySslContextProxy sslContextProxy,
        int sslHandshakeTimeout,
        int maxRequestSize
    ) {
        super(channel);
        this.sslHandshakeTimeout = sslHandshakeTimeout;
        this.sslMaxAppBufferSize = maxRequestSize * 2;
        this.sslContext = sslContextProxy.loadSslContext();
        sslContextProxy.onSslContextReload(newSslContext ->
            this.sslContext = newSslContext
        );
    }

    public boolean isHandshakeComplete() {
        return handshakeComplete.get();
    }

    @SuppressWarnings("MethodLength")
    public void handshake() throws IOException {
        if (!channel.isConnected()) {
            logger.info("channel: {} closed", channel);
            return;
        }
        if (engine != null) {
            logger.info(
                "channel: {} handshake already initiated, handshakeComplete: {}",
                channel,
                handshakeComplete
            );
            return;
        }
        engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.beginHandshake();

        SSLSession session = engine.getSession();
        appBufferSize = session.getApplicationBufferSize();
        netBufferSize = session.getPacketBufferSize();
        /*
         * netBuffer holds encrypted pending data. It must be allowed to hold at
         * least a couple of TLS packets because TCP can coalesce records, but it
         * must not grow without bound when a peer sends malformed or incomplete
         * records. The application-size limit gives us a stable upper bound.
         */
        sslMaxNetBufferSize = Math.max(netBufferSize * 2, sslMaxAppBufferSize);

        netBuffer = ByteBuffer.allocate(netBufferSize);
        appBuffer = ByteBuffer.allocate(appBufferSize);
        outboundNetBuffer = ByteBuffer.allocate(netBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerNetData = ByteBuffer.allocate(netBufferSize);

        SSLEngineResult result;
        SSLEngineResult.HandshakeStatus handshakeStatus = engine.getHandshakeStatus();
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + sslHandshakeTimeout;
        while (handshakeStatus != FINISHED && handshakeStatus != NOT_HANDSHAKING) {
            currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                throw new SSLException("Timeout");
            }
            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    int readBytes = channel.read(peerNetData);
                    if (readBytes < 0) {
                        if (engine.isInboundDone() && engine.isOutboundDone()) {
                            throw new SSLException(
                                "status is NEED_UNWRAP " +
                                    "while inbound and outbound done"
                            );
                        }
                        try {
                            engine.closeInbound();
                        } catch (SSLException e) {
                            logger.info(
                                "this engine was forced to close inbound, " +
                                    "without having received the proper SSL/TLS close " +
                                    "notification message from the peer, due to end of stream.",
                                e
                            );
                        }
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    peerNetData.flip();
                    try {
                        result = engine.unwrap(peerNetData, peerAppData);
                        peerNetData.compact();
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        logger.info(
                            "a problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection...",
                            e
                        );
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    switch (result.getStatus()) {
                        case BUFFER_OVERFLOW:
                            throw new EzyConnectionCloseException("max request size");
                        case BUFFER_UNDERFLOW:
                            break;
                        case CLOSED:
                            if (engine.isOutboundDone()) {
                                throw new SSLException("status CLOSED while outbound done");
                            } else {
                                engine.closeOutbound();
                                handshakeStatus = engine.getHandshakeStatus();
                                break;
                            }
                        default: // OK
                            break;
                    }
                    break;
                case NEED_WRAP:
                    outboundNetBuffer.clear();
                    try {
                        result = engine.wrap(EMPTY_BUFFER, outboundNetBuffer);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        logger.info(
                            "a problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection...",
                            e
                        );
                        break;
                    }
                    switch (result.getStatus()) {
                        case BUFFER_OVERFLOW:
                            outboundNetBuffer = ByteBuffer.allocate(
                                outboundNetBuffer.capacity() * 2
                            );
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException(
                                "should not happen, buffer underflow occurred after a wrap."
                            );
                        case CLOSED:
                            try {
                                writeOrTimeout(channel, outboundNetBuffer, endTime);
                                peerNetData.clear();
                            } catch (SSLException e) {
                                throw e;
                            } catch (Exception e) {
                                logger.info(
                                    "failed to send server's close message " +
                                        "due to socket channel's failure.",
                                    e
                                );
                                handshakeStatus = engine.getHandshakeStatus();
                            }
                            break;
                        default: // OK
                            writeOrTimeout(channel, outboundNetBuffer, endTime);
                            break;
                    }
                    break;
                default: // NEED_TASK:
                    Runnable task;
                    while ((task = engine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    handshakeStatus = engine.getHandshakeStatus();
                    break;
            }
        }
        /*
         * A client can send application data immediately after its final
         * handshake message. If unwrap consumed only the handshake portion,
         * peerNetData may still contain encrypted application bytes. Preserve
         * those bytes for read() instead of dropping the first request.
         */
        peerNetData.flip();
        if (peerNetData.hasRemaining()) {
            appendToNetBuffer(peerNetData);
        }
        handshakeComplete.set(true);
    }

    /**
     * Decrypts the encrypted bytes currently read from the socket.
     *
     * <p>The input buffer can contain a partial TLS record, one record, or
     * multiple coalesced records. Any encrypted bytes that cannot be unwrapped
     * yet are retained in {@link #netBuffer}. The returned array contains all
     * plaintext produced during this call and is bounded by
     * {@link #sslMaxAppBufferSize} to prevent memory exhaustion from oversized
     * inbound payloads.</p>
     */
    @SuppressWarnings("MethodLength")
    public byte[] read(ByteBuffer buffer) throws Exception {
        if (!handshakeComplete.get()) {
            throw new SSLException("SSL handshake not established");
        }
        if (appBuffer == null) {
            appBuffer = ByteBuffer.allocate(appBufferSize);
        }
        /*
         * Some tests initialize private fields directly. Runtime initialization
         * happens during handshake(), but this fallback keeps the invariant valid
         * if read() is exercised with reflected state.
         */
        if (sslMaxNetBufferSize <= 0) {
            sslMaxNetBufferSize = Math.max(
                netBuffer.capacity() * 2,
                sslMaxAppBufferSize
            );
        }
        appendToNetBuffer(buffer);
        netBuffer.flip();
        ByteArrayOutputStream output = null;
        while (netBuffer.hasRemaining()) {
            SSLEngineResult result = engine.unwrap(netBuffer, appBuffer);
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    /*
                     * SSLEngine produced more plaintext than appBuffer can hold.
                     * Drain what we have, grow within the configured limit, and
                     * retry with the remaining encrypted bytes.
                     */
                    output = drainAppBuffer(output);
                    appBuffer = allocateDoubleAppBuffer(appBuffer);
                    break;
                case BUFFER_UNDERFLOW:
                    /*
                     * The current encrypted bytes do not form a complete TLS
                     * record. Keep them in netBuffer and return whatever
                     * plaintext was already produced.
                     */
                    netBuffer.compact();
                    return getOutputBytes(drainAppBuffer(output));
                case CLOSED:
                    try {
                        engine.closeOutbound();
                    } catch (Throwable e) {
                        throw new EzyConnectionCloseException(
                            "ssl unwrap result status is CLOSE",
                            e
                        );
                    }
                    throw new EzyConnectionCloseException(
                        "ssl unwrap result status is CLOSE"
                    );
                default: // 0K
                    output = drainAppBuffer(output);
                    /*
                     * Defensive progress check: a valid SSLEngine result should
                     * consume input or produce output. If it does neither, stop
                     * this read cycle to avoid a tight loop.
                     */
                    if (result.bytesConsumed() == 0 && result.bytesProduced() == 0) {
                        netBuffer.compact();
                        return getOutputBytes(output);
                    }
                    break;
            }
        }
        netBuffer.compact();
        return getOutputBytes(output);
    }

    /**
     * Encrypts plaintext bytes into one or more TLS records.
     *
     * <p>SSLEngine.wrap() is not required to consume the whole input in one
     * call. This method loops until all plaintext has been wrapped and returns
     * the concatenated encrypted records.</p>
     */
    @Override
    public byte[] pack(byte[] bytes) throws Exception {
        if (!handshakeComplete.get()) {
            throw new SSLException("SSL handshake not established");
        }
        if (outboundNetBuffer == null) {
            outboundNetBuffer = ByteBuffer.allocate(netBufferSize);
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        outboundNetBuffer.clear();
        ByteArrayOutputStream output = null;
        while (buffer.hasRemaining()) {
            SSLEngineResult result = engine.wrap(
                buffer,
                outboundNetBuffer
            );
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    /*
                     * The outbound TLS record did not fit in outboundNetBuffer.
                     * Drain the encrypted bytes already produced, grow the
                     * reusable buffer, and continue wrapping the same plaintext.
                     */
                    output = drainOutboundNetBuffer(output);
                    outboundNetBuffer = allocateDoubleNetBuffer(outboundNetBuffer);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new IOException(
                        "should not happen, buffer underflow occurred after a wrap."
                    );
                case CLOSED:
                    try {
                        engine.closeOutbound();
                    } catch (Throwable e) {
                        throw new EzyConnectionCloseException(
                            "ssl wrap result status is CLOSE",
                            e
                        );
                    }
                    throw new EzyConnectionCloseException(
                        "ssl wrap result status is CLOSE"
                    );
                default: // OK
                    output = drainOutboundNetBuffer(output);
                    /*
                     * A no-progress OK result should not normally happen. Stop
                     * rather than spinning forever on a broken engine state.
                     */
                    if (result.bytesConsumed() == 0 && result.bytesProduced() == 0) {
                        return getOutputBytes(output);
                    }
                    break;
            }
        }
        return getOutputBytes(output);
    }

    public void writeOrTimeout(
        SocketChannel channel,
        ByteBuffer buffer,
        long timeoutAt
    ) throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= timeoutAt) {
                throw new SSLException("Timeout");
            }
            channel.write(buffer);
        }
    }

    /**
     * Appends encrypted inbound bytes while keeping netBuffer in write mode.
     */
    private void appendToNetBuffer(
        ByteBuffer buffer
    ) throws EzyConnectionCloseException {
        netBuffer = ensureRemaining(netBuffer, buffer.remaining());
        netBuffer.put(buffer);
    }

    /**
     * Ensures enough room for encrypted pending data.
     *
     * <p>The upper bound is important for security: a peer can intentionally
     * send incomplete or malformed TLS records. Without this cap, those bytes
     * could accumulate in heap memory until the process runs out of RAM.</p>
     */
    private ByteBuffer ensureRemaining(
        ByteBuffer byteBuffer,
        int remaining
    ) throws EzyConnectionCloseException {
        if (byteBuffer.remaining() >= remaining) {
            return byteBuffer;
        }
        int newCapacity = byteBuffer.capacity();
        int requiredCapacity = byteBuffer.position() + remaining;
        if (requiredCapacity > sslMaxNetBufferSize) {
            throw new EzyConnectionCloseException("max request size");
        }
        while (newCapacity < requiredCapacity) {
            if (newCapacity > sslMaxNetBufferSize / 2) {
                newCapacity = sslMaxNetBufferSize;
                break;
            }
            newCapacity *= 2;
        }
        ByteBuffer answer = ByteBuffer.allocate(newCapacity);
        byteBuffer.flip();
        answer.put(byteBuffer);
        return answer;
    }

    /**
     * Grows the plaintext buffer used by unwrap(), but never beyond the
     * configured inbound request limit.
     */
    private ByteBuffer allocateDoubleAppBuffer(
        ByteBuffer byteBuffer
    ) throws EzyConnectionCloseException {
        int doubleSize = byteBuffer.capacity() * 2;
        if (doubleSize > sslMaxAppBufferSize) {
            throw new EzyConnectionCloseException("max request size");
        }
        return ByteBuffer.allocate(doubleSize);
    }

    /**
     * Grows the outbound encrypted buffer. Outbound data is generated by the
     * server, so request-size limits are enforced before data reaches this path.
     */
    private ByteBuffer allocateDoubleNetBuffer(ByteBuffer byteBuffer) {
        return ByteBuffer.allocate(byteBuffer.capacity() * 2);
    }

    /**
     * Moves plaintext from appBuffer into the current read output and clears the
     * reusable buffer for the next unwrap().
     */
    private ByteArrayOutputStream drainAppBuffer(
        ByteArrayOutputStream output
    ) throws EzyConnectionCloseException {
        appBuffer.flip();
        ByteArrayOutputStream answer = drainBuffer(
            output,
            appBuffer,
            sslMaxAppBufferSize
        );
        appBuffer.clear();
        return answer;
    }

    /**
     * Moves encrypted bytes from outboundNetBuffer into the current pack output
     * and clears the reusable buffer for the next wrap().
     */
    private ByteArrayOutputStream drainOutboundNetBuffer(
        ByteArrayOutputStream output
    ) throws EzyConnectionCloseException {
        outboundNetBuffer.flip();
        ByteArrayOutputStream answer = drainBuffer(output, outboundNetBuffer, 0);
        outboundNetBuffer.clear();
        return answer;
    }

    /**
     * Copies all remaining bytes from a ByteBuffer into a byte-array output.
     *
     * @param maxSize maximum total output size, or {@code 0} for no limit
     */
    private ByteArrayOutputStream drainBuffer(
        ByteArrayOutputStream output,
        ByteBuffer buffer,
        int maxSize
    ) throws EzyConnectionCloseException {
        if (!buffer.hasRemaining()) {
            return output;
        }
        if (maxSize > 0
            && output != null
            && output.size() + buffer.remaining() > maxSize
        ) {
            throw new EzyConnectionCloseException("max request size");
        }
        ByteArrayOutputStream answer = output;
        if (answer == null) {
            if (maxSize > 0 && buffer.remaining() > maxSize) {
                throw new EzyConnectionCloseException("max request size");
            }
            answer = new ByteArrayOutputStream(buffer.remaining());
        }
        while (buffer.hasRemaining()) {
            answer.write(buffer.get());
        }
        return answer;
    }

    private byte[] getOutputBytes(ByteArrayOutputStream output) {
        return output != null ? output.toByteArray() : new byte[0];
    }

    @Override
    public void close() {
        super.close();
        if (engine != null) {
            processWithLogException(engine::closeOutbound);
        }
    }
}
