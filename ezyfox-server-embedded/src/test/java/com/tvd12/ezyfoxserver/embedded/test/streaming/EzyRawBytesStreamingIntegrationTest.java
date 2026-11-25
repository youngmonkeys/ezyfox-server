package com.tvd12.ezyfoxserver.embedded.test.streaming;

import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.embedded.test.compression.CompressionRawClient;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyStreamingEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUdpSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.ByteBuffer;

public class EzyRawBytesStreamingIntegrationTest {

    private static final String HOST = "127.0.0.1";
    private static final String ZONE_NAME = "streaming";
    private static final long EVENT_TIMEOUT_MILLIS = 5000;
    private static final long NO_EVENT_TIMEOUT_MILLIS = 1000;

    @BeforeMethod
    public void clearEvents() {
        StreamingRecorderPluginEntry.clearEvents();
    }

    @Test
    public void tcpRawBytesTest() throws Exception {
        try (TestServer server = new TestServer(true)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "tcp_raw_bytes");
                byte[] frame = newRawFrame(true, newPayload(3000));

                // when
                client.sendTcpBytes(frame);

                // then
                EzyStreamingEvent event = pollEvent();
                Asserts.assertEquals(event.getBytes(), frame);
                long expectedReadBytes = client.getTcpSentBytes();
                Asserts.assertEquals(
                    awaitReadBytes(event.getSession(), expectedReadBytes),
                    expectedReadBytes
                );
            }
        }
    }

    @Test
    public void tcpRawBytesSmallSizeTest() throws Exception {
        try (TestServer server = new TestServer(true)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "tcp_raw_bytes_small_size");
                byte[] frame = newRawFrame(false, newPayload(100));

                // when
                client.sendTcpBytes(frame);

                // then
                EzyStreamingEvent event = pollEvent();
                Asserts.assertEquals(event.getBytes(), frame);
                long expectedReadBytes = client.getTcpSentBytes();
                Asserts.assertEquals(
                    awaitReadBytes(event.getSession(), expectedReadBytes),
                    expectedReadBytes
                );
            }
        }
    }

    @Test
    public void udpRawBytesTest() throws Exception {
        try (TestServer server = new TestServer(true)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "udp_raw_bytes");
                client.udpHandshake(server.udpPort);
                byte[] frame = newRawFrame(false, newPayload(512));

                // when
                client.sendUdpBytes(frame);

                // then
                EzyStreamingEvent event = pollEvent();
                Asserts.assertEquals(event.getBytes(), frame);
                long expectedReadBytes = client.getTcpSentBytes() + frame.length;
                Asserts.assertEquals(
                    awaitReadBytes(event.getSession(), expectedReadBytes),
                    expectedReadBytes
                );
            }
        }
    }

    @Test
    public void tcpAndUdpReceiveSameRawBytesTest() throws Exception {
        try (TestServer server = new TestServer(true)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "tcp_udp_raw_bytes");
                client.udpHandshake(server.udpPort);
                byte[] frame = newRawFrame(false, newPayload(256));

                // when
                client.sendTcpBytes(frame);
                EzyStreamingEvent tcpEvent = pollEvent();
                client.sendUdpBytes(frame);
                EzyStreamingEvent udpEvent = pollEvent();

                // then
                Asserts.assertEquals(tcpEvent.getBytes(), frame);
                Asserts.assertEquals(udpEvent.getBytes(), tcpEvent.getBytes());
                long expectedReadBytes = client.getTcpSentBytes() + frame.length;
                Asserts.assertEquals(
                    awaitReadBytes(udpEvent.getSession(), expectedReadBytes),
                    expectedReadBytes
                );
            }
        }
    }

    @Test
    public void streamingDisabledTest() throws Exception {
        try (TestServer server = new TestServer(false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "streaming_disabled");
                byte[] frame = newRawFrame(true, newPayload(100));

                // when
                client.sendTcpBytes(frame);

                // then
                Asserts.assertNull(
                    StreamingRecorderPluginEntry.pollEvent(NO_EVENT_TIMEOUT_MILLIS)
                );
            }
        }
    }

    private static EzyStreamingEvent pollEvent() throws InterruptedException {
        EzyStreamingEvent event = StreamingRecorderPluginEntry.pollEvent(
            EVENT_TIMEOUT_MILLIS
        );
        if (event == null) {
            throw new AssertionError("has no streaming event after " + EVENT_TIMEOUT_MILLIS + "ms");
        }
        return event;
    }

    private static long awaitReadBytes(
        EzySession session,
        long expected
    ) throws InterruptedException {
        long deadline = System.currentTimeMillis() + EVENT_TIMEOUT_MILLIS;
        while (session.getReadBytes() != expected
            && System.currentTimeMillis() < deadline
        ) {
            Thread.sleep(20);
        }
        return session.getReadBytes();
    }

    private static byte[] newRawFrame(boolean bigSize, byte[] payload) {
        int sizeLength = bigSize ? 4 : 2;
        ByteBuffer buffer = ByteBuffer.allocate(1 + sizeLength + payload.length);
        buffer.put((byte) ((1 << 4) | (bigSize ? 1 : 0)));
        if (bigSize) {
            buffer.putInt(payload.length);
        } else {
            buffer.putShort((short) payload.length);
        }
        buffer.put(payload);
        return buffer.array();
    }

    private static byte[] newPayload(int size) {
        byte[] payload = new byte[size];
        for (int i = 0; i < size; ++i) {
            payload[i] = (byte) i;
        }
        return payload;
    }

    private static int freeTcpPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static int freeUdpPort() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static class TestServer implements AutoCloseable {

        private final int socketPort;
        private final int udpPort;
        private final EzyEmbeddedServer server;

        TestServer(boolean streamingEnable) throws Exception {
            this.socketPort = freeTcpPort();
            this.udpPort = freeUdpPort();
            EzySimplePluginSetting pluginSetting = new EzyPluginSettingBuilder()
                .name("streaming-recorder")
                .entryLoader(StreamingRecorderPluginEntry.Loader.class)
                .build();
            EzySimpleStreamingSetting streamingSetting = new EzySimpleStreamingSetting();
            streamingSetting.setEnable(streamingEnable);
            EzySimpleSettings settings = new EzySettingsBuilder()
                .streaming(streamingSetting)
                .socket(
                    new EzySocketSettingBuilder()
                        .address(HOST)
                        .port(socketPort)
                        .maxRequestSize(65536)
                        .build()
                )
                .udp(
                    new EzyUdpSettingBuilder()
                        .active(true)
                        .address(HOST)
                        .port(udpPort)
                        .build()
                )
                .websocket(
                    new EzyWebSocketSettingBuilder()
                        .active(false)
                        .build()
                )
                .zone(
                    new EzyZoneSettingBuilder()
                        .name(ZONE_NAME)
                        .streaming(streamingSetting)
                        .plugin(pluginSetting)
                        .build()
                )
                .build();
            this.server = EzyEmbeddedServer.builder()
                .settings(settings)
                .build();
            this.server.start();
        }

        CompressionRawClient newClient() throws IOException {
            return new CompressionRawClient(HOST, socketPort);
        }

        @Override
        public void close() {
            server.stop();
        }
    }
}
