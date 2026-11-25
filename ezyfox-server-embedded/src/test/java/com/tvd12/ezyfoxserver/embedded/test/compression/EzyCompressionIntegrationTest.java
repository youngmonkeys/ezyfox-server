package com.tvd12.ezyfoxserver.embedded.test.compression;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
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

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class EzyCompressionIntegrationTest {

    private static final String HOST = "127.0.0.1";
    private static final String ZONE_NAME = "compression";
    private static final String LONG_PAYLOAD = newLongPayload();
    private static final String SHORT_PAYLOAD = "hello";

    @Test
    public void tcpCompressionTest() throws Exception {
        try (TestServer server = new TestServer(true, false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, true);
                client.login(ZONE_NAME, "tcp_compression");

                // when
                Received longResponse = client.echo(server.pluginId, LONG_PAYLOAD, false, true);
                Received shortResponse = client.echo(server.pluginId, SHORT_PAYLOAD, false, false);

                // then
                Asserts.assertTrue(client.isServerCompressionEnable());
                Asserts.assertEquals(longResponse.command(), EzyCommand.PLUGIN_REQUEST.getId());
                Asserts.assertEquals(longResponse.echoPayload(), LONG_PAYLOAD);
                Asserts.assertTrue(longResponse.isCompressed());
                Asserts.assertTrue(longResponse.wireSize() < LONG_PAYLOAD.length());
                Asserts.assertEquals(shortResponse.echoPayload(), SHORT_PAYLOAD);
                Asserts.assertFalse(shortResponse.isCompressed());
            }
        }
    }

    @Test
    public void tcpCompressionWithEncryptionTest() throws Exception {
        try (TestServer server = new TestServer(true, true)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(true, true);
                client.login(ZONE_NAME, "tcp_compression_encryption");

                // when
                Received response = client.echo(server.pluginId, LONG_PAYLOAD, true, true);

                // then
                Asserts.assertTrue(client.isServerCompressionEnable());
                Asserts.assertEquals(response.echoPayload(), LONG_PAYLOAD);
                Asserts.assertTrue(response.isCompressed());
                Asserts.assertTrue(response.isEncrypted());
                Asserts.assertTrue(response.wireSize() < LONG_PAYLOAD.length());
            }
        }
    }

    @Test
    public void udpCompressionTest() throws Exception {
        try (TestServer server = new TestServer(true, false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, true);
                client.login(ZONE_NAME, "udp_compression");
                client.udpHandshake(server.udpPort);

                // when
                Received response = client.udpEcho(server.pluginId, LONG_PAYLOAD, true);

                // then
                Asserts.assertEquals(response.echoPayload(), LONG_PAYLOAD);
                Asserts.assertTrue(response.isCompressed());
                Asserts.assertTrue(response.wireSize() < LONG_PAYLOAD.length());
            }
        }
    }

    @Test
    public void serverCompressionDisabledTest() throws Exception {
        try (TestServer server = new TestServer(false, false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, true);
                client.login(ZONE_NAME, "server_disabled");

                // when
                Received response = client.echo(server.pluginId, LONG_PAYLOAD, false, false);

                // then
                Asserts.assertFalse(client.isServerCompressionEnable());
                Asserts.assertEquals(response.echoPayload(), LONG_PAYLOAD);
                Asserts.assertFalse(response.isCompressed());
            }
        }
    }

    @Test
    public void clientNotRequestCompressionTest() throws Exception {
        try (TestServer server = new TestServer(true, false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "client_not_request");

                // when
                Received response = client.echo(server.pluginId, LONG_PAYLOAD, false, false);

                // then
                Asserts.assertFalse(client.isServerCompressionEnable());
                Asserts.assertEquals(response.echoPayload(), LONG_PAYLOAD);
                Asserts.assertFalse(response.isCompressed());
            }
        }
    }

    @Test
    public void oldClientTest() throws Exception {
        try (TestServer server = new TestServer(true, false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                Received handshakeResponse = client.oldHandshake();
                client.login(ZONE_NAME, "old_client");

                // when
                Received response = client.echo(server.pluginId, LONG_PAYLOAD, false, false);

                // then
                Asserts.assertFalse(handshakeResponse.params().get(4, boolean.class));
                Asserts.assertEquals(response.echoPayload(), LONG_PAYLOAD);
                Asserts.assertFalse(response.isCompressed());
            }
        }
    }

    @Test
    public void compressedRequestWithoutNegotiationTest() throws Exception {
        try (TestServer server = new TestServer(true, false)) {
            try (CompressionRawClient client = server.newClient()) {
                // given
                client.handshake(false, false);
                client.login(ZONE_NAME, "without_negotiation");

                // when
                Received response = client.echo(server.pluginId, LONG_PAYLOAD, false, true);

                // then
                Asserts.assertEquals(response.echoPayload(), LONG_PAYLOAD);
                Asserts.assertFalse(response.isCompressed());
            }
        }
    }

    private static String newLongPayload() {
        StringBuilder builder = new StringBuilder();
        while (builder.length() < 4000) {
            builder.append("ezyfox compression integration test ");
        }
        return builder.toString();
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
        private final int pluginId;
        private final EzyEmbeddedServer server;

        TestServer(boolean compressionEnable, boolean sslActive) throws Exception {
            this.socketPort = freeTcpPort();
            this.udpPort = freeUdpPort();
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
                        .sslActive(sslActive)
                        .maxRequestSize(65536)
                        .compressionEnable(compressionEnable)
                        .compressionThreshold(512)
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
