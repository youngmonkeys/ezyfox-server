package com.tvd12.ezyfoxserver.embedded.test.client;

import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzySslType;
import com.tvd12.ezyfoxserver.client.ssl.EzySslHandshakeVerificationException;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Collections;

import static com.tvd12.ezyfoxserver.embedded.test.client.JavaClientTestServer.HOST;

public class EzyJavaClientIntegrationTest {

    private static final String LONG_PAYLOAD = newLongPayload();
    private static final String SHORT_PAYLOAD = "hello";

    @Test
    public void tcpCompressionTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder().build();
            CountingTcpProxy proxy = new CountingTcpProxy(HOST, server.getSocketPort());
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("tcp-compression")
                    .enableCompression()
                    .build(),
                false
            )
        ) {
            // given
            assertLoggedIn(client, client.connectAndLogin(HOST, proxy.getPort(), "tcp_compression"));

            // when
            WireSize wireSize = new WireSize(proxy);
            String longResponse = client.echo(server.getPluginId(), LONG_PAYLOAD, false);
            wireSize.end();
            String shortResponse = client.echo(server.getPluginId(), SHORT_PAYLOAD, false);

            // then
            Assert.assertTrue(client.isTcpCompressionEnable());
            Assert.assertEquals(longResponse, LONG_PAYLOAD);
            Assert.assertEquals(shortResponse, SHORT_PAYLOAD);
            wireSize.assertCompressed();
        }
    }

    @Test
    public void tcpCompressionWithCustomizationSslTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            CountingTcpProxy proxy = new CountingTcpProxy(HOST, server.getSocketPort());
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("tcp-compression-ssl")
                    .enableSSL()
                    .sslType(EzySslType.CUSTOMIZATION)
                    .enableCompression()
                    .build(),
                false
            )
        ) {
            // given
            assertLoggedIn(client, client.connectAndLogin(HOST, proxy.getPort(), "tcp_compression_ssl"));

            // when
            WireSize wireSize = new WireSize(proxy);
            String longResponse = client.echo(server.getPluginId(), LONG_PAYLOAD, true);
            wireSize.end();
            String shortResponse = client.echo(server.getPluginId(), SHORT_PAYLOAD, true);

            // then
            Assert.assertNotNull(client.getClient().getSessionKey());
            Assert.assertTrue(client.isTcpCompressionEnable());
            Assert.assertEquals(longResponse, LONG_PAYLOAD);
            Assert.assertEquals(shortResponse, SHORT_PAYLOAD);
            wireSize.assertCompressed();
        }
    }

    @Test
    public void udpCompressionTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder().build();
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("udp-compression")
                    .enableCompression()
                    .build(),
                true
            )
        ) {
            // given
            assertLoggedIn(client, client.connectAndLogin(HOST, server.getSocketPort(), "udp_compression"));
            Assert.assertTrue(client.udpConnect(server.getUdpPort()), client.getError());

            // when
            String longResponse = client.udpEcho(server.getPluginId(), LONG_PAYLOAD);
            String shortResponse = client.udpEcho(server.getPluginId(), SHORT_PAYLOAD);

            // then
            Assert.assertTrue(client.isTcpCompressionEnable());
            Assert.assertTrue(client.isUdpCompressionEnable());
            Assert.assertEquals(longResponse, LONG_PAYLOAD);
            Assert.assertEquals(shortResponse, SHORT_PAYLOAD);
        }
    }

    @Test
    public void serverCompressionDisabledTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .compressionEnable(false)
                .build();
            CountingTcpProxy proxy = new CountingTcpProxy(HOST, server.getSocketPort());
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("server-compression-disabled")
                    .enableCompression()
                    .build(),
                true
            )
        ) {
            // given
            assertLoggedIn(client, client.connectAndLogin(HOST, proxy.getPort(), "server_disabled"));

            // when
            WireSize wireSize = new WireSize(proxy);
            String response = client.echo(server.getPluginId(), LONG_PAYLOAD, false);
            wireSize.end();

            // then
            Assert.assertFalse(client.isTcpCompressionEnable());
            Assert.assertFalse(client.isUdpCompressionEnable());
            Assert.assertEquals(response, LONG_PAYLOAD);
            wireSize.assertNotCompressed();
        }
    }

    @Test
    public void clientNotRequestCompressionTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder().build();
            CountingTcpProxy proxy = new CountingTcpProxy(HOST, server.getSocketPort());
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("client-not-request-compression")
                    .build(),
                false
            )
        ) {
            // given
            assertLoggedIn(client, client.connectAndLogin(HOST, proxy.getPort(), "client_not_request"));

            // when
            WireSize wireSize = new WireSize(proxy);
            String response = client.echo(server.getPluginId(), LONG_PAYLOAD, false);
            wireSize.end();

            // then
            Assert.assertFalse(client.isTcpCompressionEnable());
            Assert.assertEquals(response, LONG_PAYLOAD);
            wireSize.assertNotCompressed();
        }
    }

    @Test
    public void connectAgainToServerWithoutCompressionTest() throws Exception {
        try (
            JavaClientTestServer compressionServer = JavaClientTestServer.builder().build();
            JavaClientTestServer plainServer = JavaClientTestServer.builder()
                .compressionEnable(false)
                .build();
            CountingTcpProxy plainProxy = new CountingTcpProxy(HOST, plainServer.getSocketPort());
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("connect-again")
                    .enableCompression()
                    .build(),
                false
            )
        ) {
            // given
            assertLoggedIn(
                client,
                client.connectAndLogin(HOST, compressionServer.getSocketPort(), "connect_again")
            );
            Assert.assertEquals(
                client.echo(compressionServer.getPluginId(), LONG_PAYLOAD, false),
                LONG_PAYLOAD
            );
            Assert.assertTrue(client.isTcpCompressionEnable());
            Assert.assertTrue(client.disconnect(), client.getError());

            // when
            assertLoggedIn(client, client.connectAndLogin(HOST, plainProxy.getPort(), "connect_again"));
            WireSize wireSize = new WireSize(plainProxy);
            String response = client.echo(plainServer.getPluginId(), LONG_PAYLOAD, false);
            wireSize.end();

            // then
            Assert.assertFalse(client.isTcpCompressionEnable());
            Assert.assertEquals(response, LONG_PAYLOAD);
            wireSize.assertNotCompressed();
        }
    }

    @Test
    public void verifySslHandshakeWithHostNameTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            JavaClientTester client = new JavaClientTester(
                newVerifyConfigBuilder("verify-host-name")
                    .sslHandshakeHostName("localhost")
                    .sslHandshakeTrustManager(newTestTrustManager())
                    .enableCompression()
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin(HOST, server.getSocketPort(), "verify_host_name");

            // then
            assertLoggedIn(client, loggedIn);
            Assert.assertNotNull(client.getClient().getSessionKey());
            Assert.assertTrue(client.isTcpCompressionEnable());
            Assert.assertEquals(
                client.echo(server.getPluginId(), LONG_PAYLOAD, true),
                LONG_PAYLOAD
            );
        }
    }

    @Test
    public void verifySslHandshakeWithConnectHostTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            JavaClientTester client = new JavaClientTester(
                newVerifyConfigBuilder("verify-connect-host")
                    .sslHandshakeTrustManager(newTestTrustManager())
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin("localhost", server.getSocketPort(), "verify_connect_host");

            // then
            assertLoggedIn(client, loggedIn);
            Assert.assertNotNull(client.getClient().getSessionKey());
        }
    }

    @Test
    public void verifySslHandshakeWrongHostNameTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            JavaClientTester client = new JavaClientTester(
                newVerifyConfigBuilder("verify-wrong-host")
                    .sslHandshakeHostName("evil.com")
                    .sslHandshakeTrustManager(newTestTrustManager())
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin(HOST, server.getSocketPort(), "verify_wrong_host");

            // then
            assertHandshakeRejected(client, loggedIn, "does not match host name: evil.com");
        }
    }

    @Test
    public void verifySslHandshakeIpHostWithoutHostNameTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            JavaClientTester client = new JavaClientTester(
                newVerifyConfigBuilder("verify-ip-host")
                    .sslHandshakeTrustManager(newTestTrustManager())
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin(HOST, server.getSocketPort(), "verify_ip_host");

            // then
            assertHandshakeRejected(client, loggedIn, "does not match host name: " + HOST);
        }
    }

    @Test
    public void verifySslHandshakeUntrustedCertificateTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            JavaClientTester client = new JavaClientTester(
                newVerifyConfigBuilder("verify-untrusted")
                    .sslHandshakeHostName("localhost")
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin(HOST, server.getSocketPort(), "verify_untrusted");

            // then
            assertHandshakeRejected(client, loggedIn, "server certificate is not trusted");
        }
    }

    @Test
    public void verifySslHandshakeServerNotSignTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .sslHandshakeSigning(false)
                .build();
            JavaClientTester client = new JavaClientTester(
                newVerifyConfigBuilder("verify-server-not-sign")
                    .sslHandshakeHostName("localhost")
                    .sslHandshakeTrustManager(newTestTrustManager())
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin(HOST, server.getSocketPort(), "verify_not_sign");

            // then
            assertHandshakeRejected(client, loggedIn, "server did not sign the handshake");
        }
    }

    @Test
    public void notVerifySslHandshakeTest() throws Exception {
        try (
            JavaClientTestServer server = JavaClientTestServer.builder()
                .sslActive(true)
                .build();
            JavaClientTester client = new JavaClientTester(
                JavaClientTester.newConfigBuilder("not-verify")
                    .enableSSL()
                    .sslType(EzySslType.CUSTOMIZATION)
                    .build(),
                false
            )
        ) {
            // when
            boolean loggedIn = client.connectAndLogin(HOST, server.getSocketPort(), "not_verify");

            // then
            assertLoggedIn(client, loggedIn);
            Assert.assertNotNull(client.getClient().getSessionKey());
            Assert.assertEquals(
                client.echo(server.getPluginId(), SHORT_PAYLOAD, true),
                SHORT_PAYLOAD
            );
        }
    }

    private static EzyClientConfig.Builder newVerifyConfigBuilder(String clientName) {
        return JavaClientTester.newConfigBuilder(clientName)
            .enableSSL()
            .sslType(EzySslType.CUSTOMIZATION)
            .verifySslHandshake();
    }

    private static void assertLoggedIn(JavaClientTester client, boolean loggedIn) {
        Assert.assertTrue(loggedIn, "login failed: " + client.getError());
    }

    private static void assertHandshakeRejected(
        JavaClientTester client,
        boolean loggedIn,
        String expectedMessage
    ) {
        Assert.assertFalse(loggedIn);
        Assert.assertFalse(client.isLoggedIn());
        Assert.assertFalse(client.isHandshaked());
        RuntimeException error = client.getHandshakeError();
        Assert.assertNotNull(error, "expected handshake error, but: " + client.getError());
        Assert.assertEquals(error.getClass(), EzySslHandshakeVerificationException.class);
        Assert.assertTrue(
            error.getMessage().contains(expectedMessage),
            "unexpected message: " + error.getMessage()
        );
        Assert.assertNull(client.getClient().getSessionKey());
    }

    private static X509TrustManager newTestTrustManager() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream stream = EzyJavaClientIntegrationTest.class
            .getClassLoader()
            .getResourceAsStream(JavaClientTestServer.KEYSTORE_RESOURCE)
        ) {
            keyStore.load(stream, JavaClientTestServer.KEYSTORE_PASSWORD.toCharArray());
        }
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        for (String alias : Collections.list(keyStore.aliases())) {
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate != null) {
                trustStore.setCertificateEntry(alias, certificate);
            }
        }
        TrustManagerFactory factory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        factory.init(trustStore);
        for (TrustManager trustManager : factory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new IllegalStateException("there is no x509 trust manager");
    }

    private static String newLongPayload() {
        StringBuilder builder = new StringBuilder();
        while (builder.length() < 4000) {
            builder.append("ezyfox java client integration test ");
        }
        return builder.toString();
    }

    private static class WireSize {
        private final CountingTcpProxy proxy;
        private final long upstreamStart;
        private final long downstreamStart;
        private long upstream;
        private long downstream;

        WireSize(CountingTcpProxy proxy) {
            this.proxy = proxy;
            this.upstreamStart = proxy.getUpstreamBytes();
            this.downstreamStart = proxy.getDownstreamBytes();
        }

        void end() {
            this.upstream = proxy.getUpstreamBytes() - upstreamStart;
            this.downstream = proxy.getDownstreamBytes() - downstreamStart;
        }

        void assertCompressed() {
            int limit = LONG_PAYLOAD.length() / 4;
            Assert.assertTrue(upstream > 0 && upstream < limit, "upstream bytes: " + upstream);
            Assert.assertTrue(downstream > 0 && downstream < limit, "downstream bytes: " + downstream);
        }

        void assertNotCompressed() {
            int limit = LONG_PAYLOAD.length();
            Assert.assertTrue(upstream > limit, "upstream bytes: " + upstream);
            Assert.assertTrue(downstream > limit, "downstream bytes: " + downstream);
        }
    }
}
