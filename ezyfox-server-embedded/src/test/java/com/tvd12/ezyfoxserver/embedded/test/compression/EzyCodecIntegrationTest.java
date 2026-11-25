package com.tvd12.ezyfoxserver.embedded.test.compression;

import com.tvd12.ezyfox.codec.EzySimpleMessage;
import com.tvd12.ezyfox.codec.EzySimpleMessageHeader;
import com.tvd12.ezyfox.codec.EzySimpleMessageToBytes;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.embedded.test.compression.CompressionRawClient.Received;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUdpSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EzyCodecIntegrationTest {

    private static final String HOST = "127.0.0.1";
    private static final String ZONE_NAME = "codec";
    private static final int MAX_REQUEST_SIZE = 256 * 1024;
    private static final String LARGE_PAYLOAD = newPayload(100_000);
    private static final String BOMB_PAYLOAD = newPayload(MAX_REQUEST_SIZE * 2);

    @Test
    public void fragmentedLargeRequestTest() throws Exception {
        try (TestServer server = new TestServer()) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "fragmented_large");
                byte[] bytes = client.newEchoBytes(server.pluginId, LARGE_PAYLOAD, false, false);

                // when
                sendInFragments(client, bytes);
                Received response = client.readTcp();

                // then
                Asserts.assertTrue(bytes.length > 65535);
                Asserts.assertEquals(response.echoPayload(), LARGE_PAYLOAD);
                Asserts.assertFalse(response.isCompressed());
            }
        }
    }

    @Test
    public void fragmentedCompressedRequestTest() throws Exception {
        try (TestServer server = new TestServer()) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, true);
                client.login(ZONE_NAME, "fragmented_compressed");
                byte[] bytes = client.newEchoBytes(server.pluginId, LARGE_PAYLOAD, false, true);

                // when
                sendInFragments(client, bytes);
                Received response = client.readTcp();

                // then
                Asserts.assertTrue(bytes.length < LARGE_PAYLOAD.length());
                Asserts.assertEquals(response.echoPayload(), LARGE_PAYLOAD);
                Asserts.assertTrue(response.isCompressed());
            }
        }
    }

    @Test
    public void coalescedRequestsTest() throws Exception {
        try (TestServer server = new TestServer()) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, true);
                client.login(ZONE_NAME, "coalesced");
                String shortPayload = "hello";
                String compressedPayload = "compressed " + LARGE_PAYLOAD;
                String plainPayload = "plain " + LARGE_PAYLOAD;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                stream.write(client.newEchoBytes(server.pluginId, shortPayload, false, false));
                stream.write(client.newEchoBytes(server.pluginId, compressedPayload, false, true));
                stream.write(client.newEchoBytes(server.pluginId, plainPayload, false, false));
                byte[] bytes = stream.toByteArray();
                int splitIndex = bytes.length - 100;

                // when
                client.sendTcpBytes(Arrays.copyOfRange(bytes, 0, splitIndex));
                Thread.sleep(50);
                client.sendTcpBytes(Arrays.copyOfRange(bytes, splitIndex, bytes.length));
                Set<String> payloads = new HashSet<>();
                for (int i = 0; i < 3; ++i) {
                    payloads.add(client.readTcp().echoPayload());
                }

                // then
                Asserts.assertEquals(
                    payloads,
                    new HashSet<>(Arrays.asList(shortPayload, compressedPayload, plainPayload))
                );
            }
        }
    }

    @Test
    public void decompressionBombTest() throws Exception {
        try (TestServer server = new TestServer()) {
            try (CompressionRawClient attacker = server.newClient()) {
                // given
                attacker.handshake(false, true);
                attacker.login(ZONE_NAME, "bomb_attacker");
                byte[] bytes = attacker.newEchoBytes(server.pluginId, BOMB_PAYLOAD, false, true);

                // when
                attacker.sendTcpBytes(bytes);
                boolean disconnected = attacker.waitDisconnected();

                // then
                Asserts.assertTrue(bytes.length < MAX_REQUEST_SIZE);
                Asserts.assertTrue(disconnected);
            }
            try (CompressionRawClient client = server.newClient()) {
                client.handshake(false, true);
                client.login(ZONE_NAME, "bomb_after");
                Received response = client.echo(server.pluginId, LARGE_PAYLOAD, false, true);
                Asserts.assertEquals(response.echoPayload(), LARGE_PAYLOAD);
            }
        }
    }

    @Test
    public void invalidCompressedContentTest() throws Exception {
        try (TestServer server = new TestServer()) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, true);
                client.login(ZONE_NAME, "invalid_compressed");
                byte[] invalidContent = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
                byte[] invalidBytes = new EzySimpleMessageToBytes().convert(
                    new EzySimpleMessage(
                        new EzySimpleMessageHeader(false, false, true, false, false, false),
                        invalidContent,
                        invalidContent.length
                    )
                );

                // when
                client.sendTcpBytes(invalidBytes);
                Received response = client.echo(server.pluginId, LARGE_PAYLOAD, false, true);

                // then
                Asserts.assertEquals(response.echoPayload(), LARGE_PAYLOAD);
                Asserts.assertTrue(response.isCompressed());
            }
        }
    }

    private static void sendInFragments(
        CompressionRawClient client,
        byte[] bytes
    ) throws Exception {
        int[] firstCuts = {1, 2, 4, 7};
        int offset = 0;
        for (int cut : firstCuts) {
            client.sendTcpBytes(Arrays.copyOfRange(bytes, offset, cut));
            offset = cut;
            Thread.sleep(20);
        }
        while (offset < bytes.length) {
            int end = Math.min(bytes.length, offset + 4093);
            client.sendTcpBytes(Arrays.copyOfRange(bytes, offset, end));
            offset = end;
            Thread.sleep(2);
        }
    }

    private static String newPayload(int length) {
        StringBuilder builder = new StringBuilder();
        while (builder.length() < length) {
            builder.append("ezyfox codec integration test ");
        }
        return builder.substring(0, length);
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
        private final int pluginId;
        private final EzyEmbeddedServer server;

        TestServer() throws Exception {
            this.socketPort = freeTcpPort();
            EzySimplePluginSetting pluginSetting = new EzyPluginSettingBuilder()
                .name("echo")
                .entryLoader(CompressionEchoPluginEntry.Loader.class)
                .build();
            this.pluginId = pluginSetting.getId();
            EzySimpleSettings settings = new EzySettingsBuilder()
                .socket(
                    new EzySocketSettingBuilder()
                        .address(HOST)
                        .port(socketPort)
                        .maxRequestSize(MAX_REQUEST_SIZE)
                        .compressionEnable(true)
                        .compressionThreshold(512)
                        .build()
                )
                .udp(
                    new EzyUdpSettingBuilder()
                        .active(false)
                        .address(HOST)
                        .port(freeUdpPort())
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
