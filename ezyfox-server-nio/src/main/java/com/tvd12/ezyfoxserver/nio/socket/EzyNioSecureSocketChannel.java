package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import static com.tvd12.ezyfoxserver.ssl.EzySslEngines.safeCloseOutbound;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBuffer;

public class EzyNioSecureSocketChannel extends EzyNioSocketChannel {

    @Getter
    private SSLEngine engine;
    private SSLSession session;
    @Getter
    private ByteBuffer readAppBuffer;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EzyNioSecureSocketChannel(
        SocketChannel channel
    ) {
        super(channel);
    }

    public void setEngine(SSLEngine engine) {
        logger.info("channel: {} set engine status: {}", channel, engine.getHandshakeStatus());
        this.engine = engine;
        this.session = engine.getSession();
        this.readAppBuffer = ByteBuffer.allocate(session.getApplicationBufferSize());
    }

    @Override
    public byte[] pack(byte[] bytes) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        int netBufferLength = session.getPacketBufferSize();
        ByteBuffer netBuffer = ByteBuffer.allocate(netBufferLength);
        while (buffer.hasRemaining()) {
            SSLEngineResult result = engine.wrap(
                buffer,
                netBuffer
            );
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    netBuffer = enlargeBuffer(netBuffer, netBufferLength);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new IOException("Buffer underflow occurred after a wrap");
                case CLOSED:
                    safeCloseOutbound(engine);
                    throw new EzyConnectionCloseException(
                        "ssl wrap result status is CLOSE"
                    );
                default: // OK
                    netBuffer.flip();
                    byte[] answer = new byte[netBuffer.limit()];
                    netBuffer.get(answer);
                    logger.info(
                        "wrap on channel: {}, from size: {}, to size: {} - {}, status: {} - {}, engine: {}",
                        channel,
                        bytes.length,
                        answer.length,
                        Arrays.toString(answer),
                        result.getHandshakeStatus(),
                        engine.getHandshakeStatus(),
                        engine
                    );
                    return answer;
            }
        }
        return bytes;
    }

    @Override
    public void close() {
        super.close();
        this.engine = null;
        this.readAppBuffer = null;
    }
}
