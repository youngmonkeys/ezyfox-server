package com.tvd12.ezyfoxserver.embedded.test.compression;

import com.tvd12.ezyfox.codec.EzyByteToObjectDecoder;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageHeader;
import com.tvd12.ezyfox.codec.EzyMessageHeaderReader;
import com.tvd12.ezyfox.codec.EzyMessageReaders;
import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.codec.EzySimpleMessage;
import com.tvd12.ezyfox.codec.MsgPackCodecCreator;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.security.EzyAsyCrypt;
import com.tvd12.ezyfox.security.EzyKeysGenerator;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.constant.EzyCommand;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.UUID;

public class CompressionRawClient implements AutoCloseable {

    private final Socket socket;
    private final DataInputStream input;
    private final OutputStream output;
    private final EzyObjectToByteEncoder encoder;
    private final EzyByteToObjectDecoder decoder;
    private final String host;
    private DatagramSocket datagramSocket;
    private byte[] privateKey;
    private byte[] sessionKey;
    private long sessionId;
    private String token;
    private boolean serverCompressionEnable;
    private long tcpSentBytes;

    public CompressionRawClient(String host, int port) throws IOException {
        MsgPackCodecCreator codecCreator = new MsgPackCodecCreator(true);
        this.host = host;
        this.encoder = codecCreator.newEncoder();
        this.decoder = codecCreator.newDecoder(Integer.MAX_VALUE);
        this.socket = new Socket(host, port);
        this.socket.setSoTimeout(5000);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = socket.getOutputStream();
    }

    public Received oldHandshake() throws Exception {
        sendTcp(
            EzyEntityArrays.newArray(
                EzyCommand.HANDSHAKE.getId(),
                EzyEntityArrays.newArray(
                    UUID.randomUUID().toString(),
                    null,
                    "JAVA",
                    "1.0.0",
                    false,
                    ""
                )
            ),
            false
        );
        return readHandshake();
    }

    public Received handshake(
        boolean enableEncryption,
        boolean enableCompression
    ) throws Exception {
        byte[] clientKey = null;
        if (enableEncryption) {
            KeyPair keyPair = EzyKeysGenerator.builder().build().generate();
            clientKey = keyPair.getPublic().getEncoded();
            privateKey = keyPair.getPrivate().getEncoded();
        }
        sendTcp(
            EzyEntityArrays.newArray(
                EzyCommand.HANDSHAKE.getId(),
                EzyEntityArrays.newArray(
                    UUID.randomUUID().toString(),
                    clientKey,
                    "JAVA",
                    "1.0.0",
                    enableEncryption,
                    "",
                    null,
                    enableCompression
                )
            ),
            false
        );
        Received received = readHandshake();
        if (enableEncryption) {
            byte[] encryptedSessionKey = received.params().get(3, byte[].class);
            sessionKey = EzyAsyCrypt.builder()
                .privateKey(privateKey)
                .build()
                .decrypt(encryptedSessionKey);
        }
        return received;
    }

    private Received readHandshake() throws Exception {
        Received received = readTcp();
        EzyArray params = received.params();
        token = params.get(1, String.class);
        sessionId = params.get(2, long.class);
        serverCompressionEnable = params.get(4, boolean.class, false);
        return received;
    }

    public Received login(String zoneName, String username) throws Exception {
        sendTcp(
            EzyEntityArrays.newArray(
                EzyCommand.LOGIN.getId(),
                EzyEntityArrays.newArray(zoneName, username, "123456", null)
            ),
            false
        );
        return readTcp();
    }

    public Received echo(
        int pluginId,
        String payload,
        boolean encrypted,
        boolean compressed
    ) throws Exception {
        sendTcp(newEchoRequest(pluginId, payload, false, encrypted), encrypted, compressed);
        return readTcp();
    }

    public void udpHandshake(int udpPort) throws Exception {
        datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(5000);
        datagramSocket.connect(new InetSocketAddress(host, udpPort));
        byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
        ByteBuffer content = ByteBuffer.allocate(8 + 2 + tokenBytes.length);
        content.putLong(sessionId);
        content.putShort((short) tokenBytes.length);
        content.put(tokenBytes);
        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + content.capacity());
        buffer.put((byte) (1 << 5));
        buffer.putShort((short) content.capacity());
        buffer.put(content.array());
        datagramSocket.send(new DatagramPacket(buffer.array(), buffer.capacity()));
        Received received = readUdp();
        if (received.command() != EzyCommand.UDP_HANDSHAKE.getId()
            || received.params().get(0, int.class) != 200
        ) {
            throw new IllegalStateException("udp handshake failed: " + received);
        }
    }

    public Received udpEcho(
        int pluginId,
        String payload,
        boolean compressed
    ) throws Exception {
        byte[] bytes = encode(newEchoRequest(pluginId, payload, true, false), false, compressed);
        datagramSocket.send(new DatagramPacket(bytes, bytes.length));
        return readUdp();
    }

    private EzyArray newEchoRequest(
        int pluginId,
        String payload,
        boolean udp,
        boolean encrypted
    ) {
        return EzyEntityArrays.newArray(
            EzyCommand.PLUGIN_REQUEST.getId(),
            EzyEntityArrays.newArray(
                pluginId,
                EzyEntityArrays.newArray(udp, encrypted, payload)
            )
        );
    }

    public void sendTcp(EzyArray request, boolean encrypted) throws Exception {
        sendTcp(request, encrypted, false);
    }

    public void sendTcp(
        EzyArray request,
        boolean encrypted,
        boolean compressed
    ) throws Exception {
        sendTcpBytes(encode(request, encrypted, compressed));
    }

    public void sendTcpBytes(byte[] bytes) throws IOException {
        output.write(bytes);
        output.flush();
        tcpSentBytes += bytes.length;
    }

    public void sendUdpBytes(byte[] bytes) throws IOException {
        datagramSocket.send(new DatagramPacket(bytes, bytes.length));
    }

    public long getTcpSentBytes() {
        return tcpSentBytes;
    }

    private byte[] encode(
        EzyArray request,
        boolean encrypted,
        boolean compressed
    ) throws Exception {
        byte[] content = encoder.toMessageContent(request);
        if (compressed) {
            content = encoder.compressMessageContent(content);
        }
        return encoder.encryptMessageContent(
            content,
            encrypted ? sessionKey : null,
            compressed
        );
    }

    public Received readTcp() throws Exception {
        EzyMessageHeader header = EzyMessageHeaderReader.read(input.readByte());
        int size = header.isBigSize() ? input.readInt() : input.readUnsignedShort();
        byte[] content = new byte[size];
        input.readFully(content);
        EzyMessage message = new EzySimpleMessage(header, content, size);
        return new Received(message, (EzyArray) decoder.decode(message, sessionKey));
    }

    public Received readUdp() throws Exception {
        byte[] buffer = new byte[65536];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);
        byte[] bytes = new byte[packet.getLength()];
        System.arraycopy(buffer, 0, bytes, 0, bytes.length);
        EzyMessage message = EzyMessageReaders.bytesToMessage(bytes);
        return new Received(message, (EzyArray) decoder.decode(message, sessionKey));
    }

    public boolean isServerCompressionEnable() {
        return serverCompressionEnable;
    }

    @Override
    public void close() throws IOException {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
        socket.close();
    }

    public static class Received {

        private final EzyMessage message;
        private final EzyArray data;

        public Received(EzyMessage message, EzyArray data) {
            this.message = message;
            this.data = data;
        }

        public int command() {
            return data.get(0, int.class);
        }

        public EzyArray params() {
            return data.get(1, EzyArray.class);
        }

        public boolean isCompressed() {
            return message.getHeader().isCompressed();
        }

        public boolean isEncrypted() {
            return message.getHeader().isEncrypted();
        }

        public int wireSize() {
            return message.getByteCount();
        }

        public String echoPayload() {
            return params().get(1, EzyArray.class).get(0, String.class);
        }

        @Override
        public String toString() {
            return "Received(header: " + message.getHeader() + ", data: " + data + ")";
        }
    }
}
