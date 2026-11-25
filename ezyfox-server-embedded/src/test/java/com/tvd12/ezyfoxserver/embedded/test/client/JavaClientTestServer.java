package com.tvd12.ezyfoxserver.embedded.test.client;

import com.tvd12.ezyfoxserver.config.EzyConfigBuilder;
import com.tvd12.ezyfoxserver.constant.SslType;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.embedded.test.compression.CompressionEchoPluginEntry;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUdpSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class JavaClientTestServer implements AutoCloseable {

    public static final String HOST = "127.0.0.1";
    public static final String ZONE_NAME = "java_client";
    public static final String KEYSTORE_RESOURCE = "ssl-handshake-keystore.jks";
    public static final String KEYSTORE_PASSWORD = "123456";
    public static final int COMPRESSION_THRESHOLD = 512;

    private final int socketPort;
    private final int udpPort;
    private final int pluginId;
    private final Path homeFolder;
    private final EzyEmbeddedServer server;

    private JavaClientTestServer(Builder builder) throws Exception {
        this.socketPort = freeTcpPort();
        this.udpPort = freeUdpPort();
        this.homeFolder = newHomeFolder();
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
                    .sslActive(builder.sslActive)
                    .sslType(SslType.CUSTOMIZATION)
                    .sslHandshakeSigning(builder.sslHandshakeSigning)
                    .maxRequestSize(65536)
                    .compressionEnable(builder.compressionEnable)
                    .compressionThreshold(COMPRESSION_THRESHOLD)
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
            .config(
                new EzyConfigBuilder()
                    .ezyfoxHome(homeFolder.toString())
                    .printBanner(false)
                    .printSettings(false)
                    .build()
            )
            .settings(settings)
            .build();
        this.server.start();
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getSocketPort() {
        return socketPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public int getPluginId() {
        return pluginId;
    }

    private static Path newHomeFolder() throws IOException {
        Path home = Files.createTempDirectory("ezyfox-java-client-it");
        Path settings = Files.createDirectories(home.resolve("settings"));
        try (InputStream keystore = JavaClientTestServer.class
            .getClassLoader()
            .getResourceAsStream(KEYSTORE_RESOURCE)
        ) {
            if (keystore == null) {
                throw new IOException("not found resource: " + KEYSTORE_RESOURCE);
            }
            Files.copy(keystore, settings.resolve(KEYSTORE_RESOURCE));
        }
        Files.write(
            settings.resolve("ssl-password.txt"),
            KEYSTORE_PASSWORD.getBytes(StandardCharsets.UTF_8)
        );
        String sslConfig = "ssl.keystore=" + KEYSTORE_RESOURCE + "\n"
            + "ssl.keystore_password=ssl-password.txt\n"
            + "ssl.certificate_password=ssl-password.txt\n";
        Files.write(
            settings.resolve("ssl-config.properties"),
            sslConfig.getBytes(StandardCharsets.UTF_8)
        );
        return home;
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

    @Override
    public void close() throws IOException {
        server.stop();
        try (Stream<Path> paths = Files.walk(homeFolder)) {
            paths.sorted(Comparator.reverseOrder())
                .forEach(path -> path.toFile().delete());
        }
    }

    public static class Builder {
        private boolean compressionEnable = true;
        private boolean sslActive;
        private boolean sslHandshakeSigning = true;

        public Builder compressionEnable(boolean compressionEnable) {
            this.compressionEnable = compressionEnable;
            return this;
        }

        public Builder sslActive(boolean sslActive) {
            this.sslActive = sslActive;
            return this;
        }

        public Builder sslHandshakeSigning(boolean sslHandshakeSigning) {
            this.sslHandshakeSigning = sslHandshakeSigning;
            return this;
        }

        public JavaClientTestServer build() throws Exception {
            return new JavaClientTestServer(this);
        }
    }
}
