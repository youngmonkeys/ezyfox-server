package com.tvd12.ezyfoxserver.nio.testing.ssl;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyTcpClient;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzySslType;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyAbstractDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionFailureHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionSuccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyHandshakeHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginSuccessHandler;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.setup.EzySetup;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeSigner;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EzyRemoteSslModesClient {

    private static final String ZONE_NAME = "test";
    private static final long CASE_TIMEOUT = 15000;

    private final String host;
    private final int plainPort;
    private final int customizationPort;
    private final int certificationPort;
    private final List<String> failures = new ArrayList<>();

    public EzyRemoteSslModesClient(String[] args) {
        this.host = args.length > 0 ? args[0] : "stackask.com";
        this.plainPort = args.length > 1 ? Integer.parseInt(args[1]) : 3105;
        this.customizationPort = args.length > 2 ? Integer.parseInt(args[2]) : 3106;
        this.certificationPort = args.length > 3 ? Integer.parseInt(args[3]) : 3107;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.security.enableAIAcaIssuers", "true");
        EzyRemoteSslModesClient client = new EzyRemoteSslModesClient(args);
        int exitCode = client.run();
        System.exit(exitCode);
    }

    private int run() throws Exception {
        testPlain();
        testCustomization();
        testCertification();
        System.out.println();
        if (failures.isEmpty()) {
            System.out.println("ALL CASES PASSED");
            return 0;
        }
        System.out.println("FAILED CASES:");
        failures.forEach(it -> System.out.println("  - " + it));
        return 1;
    }

    private void testPlain() throws Exception {
        title("1. no ssl, port " + plainPort);
        CaseResult result = connectAndLogin("plain", plainPort, false, EzySslType.CUSTOMIZATION, null);
        check("plain: login success", result.loggedIn, result.describe());
        check("plain: server sends no handshake proof", result.proof == null, "proof: " + length(result.proof));
        check("plain: server sends no session key", result.encryptedSessionKey == null, "key: " + length(result.encryptedSessionKey));
    }

    private void testCustomization() throws Exception {
        title("2. customization ssl (signed handshake), port " + customizationPort);
        CaseResult result = connectAndLogin("customization", customizationPort, true, EzySslType.CUSTOMIZATION, null);
        check("customization: login success over encrypted channel", result.loggedIn, result.describe());
        check("customization: session key decrypted", result.sessionKey != null && result.sessionKey.length > 0, "key: " + length(result.sessionKey));
        check("customization: server sends handshake proof", result.proof != null, "proof: " + length(result.proof));
        if (result.proof == null) {
            return;
        }
        HandshakeProof proof = HandshakeProof.parse(result.proof);
        X509Certificate leaf = proof.certificates.get(0);
        info("proof version: " + proof.version + ", signature: " + proof.signature.length + " bytes, certificates: " + proof.certificates.size());
        info("leaf: " + leaf.getSubjectX500Principal() + ", issuer: " + leaf.getIssuerX500Principal() + ", expires: " + leaf.getNotAfter());
        check("customization: proof version", proof.version == EzySslHandshakeSigner.PROOF_VERSION, "version: " + proof.version);
        check(
            "customization: signature valid for (clientKey, encryptedSessionKey)",
            verify(leaf, proof.signature, result.clientKey, result.encryptedSessionKey),
            "signature does not match"
        );
        byte[] tamperedKey = Arrays.copyOf(result.encryptedSessionKey, result.encryptedSessionKey.length);
        tamperedKey[0] ^= 0x01;
        check(
            "customization: tampered session key is rejected",
            !verify(leaf, proof.signature, result.clientKey, tamperedKey),
            "tampered key still verifies"
        );
        byte[] otherClientKey = Arrays.copyOf(result.clientKey, result.clientKey.length);
        otherClientKey[otherClientKey.length - 1] ^= 0x01;
        check(
            "customization: proof bound to this client key",
            !verify(leaf, proof.signature, otherClientKey, result.encryptedSessionKey),
            "other client key still verifies"
        );
        check("customization: certificate matches host " + host, matchesHost(leaf, host), "san: " + leaf.getSubjectAlternativeNames());
        try {
            leaf.checkValidity();
            check("customization: certificate in validity period", true, "");
        } catch (Exception e) {
            check("customization: certificate in validity period", false, e.toString());
        }
        String chainError = validateChain(proof.certificates);
        check("customization: certificate chains to a trusted CA (AIA fetch on)", chainError == null, chainError);
        CaseResult verified = connectAndLogin(
            "customization-verified",
            customizationPort,
            true,
            EzySslType.CUSTOMIZATION,
            null,
            true,
            null
        );
        check("customization: java client verifySslHandshake() accepts server", verified.loggedIn, verified.describe());
        CaseResult wrongHost = connectAndLogin(
            "customization-wrong-host",
            customizationPort,
            true,
            EzySslType.CUSTOMIZATION,
            null,
            true,
            "evil.com"
        );
        check(
            "customization: java client verifySslHandshake() rejects wrong host",
            !wrongHost.loggedIn && wrongHost.error != null && wrongHost.error.contains("does not match host name"),
            wrongHost.describe()
        );
        if (proof.certificates.size() < 2) {
            warn("proof carries only the leaf certificate; clients without AIA fetching cannot build the chain (keystore has no intermediate)");
        }
    }

    private void testCertification() throws Exception {
        title("3. certification ssl (TLS), port " + certificationPort);
        CaseResult trustAll = connectAndLogin("certification", certificationPort, true, EzySslType.CERTIFICATION, null);
        check("certification: login success over TLS (client default trust)", trustAll.loggedIn, trustAll.describe());
        check("certification: no app-level session key", trustAll.encryptedSessionKey == null, "key: " + length(trustAll.encryptedSessionKey));

        String strictError = strictTlsHandshake(certificationPort);
        if (strictError == null) {
            check("certification: strict TLS (JDK trust store + hostname check)", true, "");
            CaseResult strict = connectAndLogin(
                "certification-strict",
                certificationPort,
                true,
                EzySslType.CERTIFICATION,
                strictSslContext()
            );
            check("certification: login success with strict trust", strict.loggedIn, strict.describe());
        } else {
            check("certification: strict TLS (JDK trust store + hostname check)", false, strictError);
        }
    }

    private CaseResult connectAndLogin(
        String name,
        int port,
        boolean enableSsl,
        EzySslType sslType,
        SSLContext sslContext
    ) throws Exception {
        return connectAndLogin(name, port, enableSsl, sslType, sslContext, false, null);
    }

    private CaseResult connectAndLogin(
        String name,
        int port,
        boolean enableSsl,
        EzySslType sslType,
        SSLContext sslContext,
        boolean verifySslHandshake,
        String sslHandshakeHostName
    ) throws Exception {
        CaseResult result = new CaseResult();
        EzyClientConfig config = EzyClientConfig.builder()
            .clientName("ssl-modes-" + name + "-" + System.nanoTime())
            .zoneName(ZONE_NAME)
            .enableSSL(enableSsl)
            .sslType(sslType)
            .verifySslHandshake(verifySslHandshake)
            .sslHandshakeHostName(sslHandshakeHostName)
            .reconnectConfigBuilder()
            .enable(false)
            .done()
            .build();
        EzyClient client = new EzyTcpClient(config);
        if (sslContext != null) {
            client.setSslContext(sslContext);
        }
        EzySetup setup = client.setup();
        setup.addEventHandler(EzyEventType.CONNECTION_SUCCESS, new EzyConnectionSuccessHandler());
        setup.addEventHandler(EzyEventType.CONNECTION_FAILURE, new EzyConnectionFailureHandler() {
            @Override
            protected void onConnectionFailed(EzyConnectionFailureEvent event) {
                result.error = "connection failure: " + event.getReason();
                result.done = true;
            }
        });
        setup.addEventHandler(EzyEventType.DISCONNECTION, event -> {
            if (!result.done) {
                result.error = "disconnected: " + event;
                result.done = true;
            }
        });
        setup.addDataHandler(EzyCommand.HANDSHAKE, new EzyHandshakeHandler() {
            @Override
            protected void preHandle(EzyArray data) {
                result.handshaked = true;
                result.proof = data.get(0, byte[].class, null);
                result.encryptedSessionKey = data.get(3, byte[].class, null);
                result.clientKey = client.getPublicKey();
                super.preHandle(data);
                result.sessionKey = client.getSessionKey();
            }

            @Override
            protected EzyRequest getLoginRequest() {
                return new EzyLoginRequest(ZONE_NAME, "tester_" + name.replace('-', '_'), "123456");
            }
        });
        setup.addDataHandler(EzyCommand.LOGIN, new EzyLoginSuccessHandler() {
            @Override
            public void handle(EzyArray data) {
                super.handle(data);
                result.loggedIn = true;
                result.username = data.get(3, String.class);
                result.done = true;
            }
        });
        setup.addDataHandler(EzyCommand.LOGIN_ERROR, new EzyAbstractDataHandler() {
            @Override
            public void handle(EzyArray data) {
                result.error = "login error: " + data;
                result.done = true;
            }
        });
        long startTime = System.currentTimeMillis();
        client.connect(host, port);
        while (!result.done && System.currentTimeMillis() - startTime < CASE_TIMEOUT) {
            try {
                client.processEvents();
            } catch (Exception e) {
                result.error = e.toString();
                result.done = true;
            }
            Thread.sleep(5);
        }
        result.elapsed = System.currentTimeMillis() - startTime;
        if (!result.done) {
            result.error = "timeout after " + CASE_TIMEOUT + "ms";
        }
        client.close();
        info(name + ": " + result.describe());
        return result;
    }

    private String strictTlsHandshake(int port) {
        try (SSLSocket socket = (SSLSocket) strictSslContext().getSocketFactory().createSocket(host, port)) {
            SSLParameters parameters = socket.getSSLParameters();
            parameters.setEndpointIdentificationAlgorithm("HTTPS");
            parameters.setServerNames(Collections.singletonList(new SNIHostName(host)));
            socket.setSSLParameters(parameters);
            socket.setSoTimeout(10000);
            socket.startHandshake();
            info("strict tls: " + socket.getSession().getProtocol() + " " + socket.getSession().getCipherSuite());
            return null;
        } catch (Exception e) {
            return e.toString();
        }
    }

    private SSLContext strictSslContext() throws Exception {
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init((KeyStore) null);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, factory.getTrustManagers(), null);
        return context;
    }

    private static boolean verify(
        X509Certificate certificate,
        byte[] signatureBytes,
        byte[] clientKey,
        byte[] encryptedSessionKey
    ) throws Exception {
        String algorithm = "RSA".equals(certificate.getPublicKey().getAlgorithm())
            ? "SHA256withRSA"
            : "SHA256withECDSA";
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(certificate.getPublicKey());
        signature.update(EzySslHandshakeSigner.signedMessage(clientKey, encryptedSessionKey));
        return signature.verify(signatureBytes);
    }

    private static boolean matchesHost(X509Certificate certificate, String host) throws Exception {
        Collection<List<?>> names = certificate.getSubjectAlternativeNames();
        if (names == null) {
            return false;
        }
        for (List<?> name : names) {
            if (((Integer) name.get(0)) != 2) {
                continue;
            }
            String dns = String.valueOf(name.get(1)).toLowerCase();
            if (dns.equals(host.toLowerCase())) {
                return true;
            }
            if (dns.startsWith("*.") && host.toLowerCase().endsWith(dns.substring(1))
                && host.indexOf('.') == host.length() - dns.length() + 1) {
                return true;
            }
        }
        return false;
    }

    private static String validateChain(List<X509Certificate> certificates) {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            int index = 0;
            for (TrustManager trustManager : factory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    for (X509Certificate ca : ((X509TrustManager) trustManager).getAcceptedIssuers()) {
                        trustStore.setCertificateEntry("ca-" + (index++), ca);
                    }
                }
            }
            X509CertSelector selector = new X509CertSelector();
            selector.setCertificate(certificates.get(0));
            PKIXBuilderParameters parameters = new PKIXBuilderParameters(trustStore, selector);
            parameters.setRevocationEnabled(false);
            parameters.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(certificates)));
            CertPathBuilder.getInstance("PKIX").build(parameters);
            return null;
        } catch (Exception e) {
            return e.toString();
        }
    }

    private void check(String name, boolean passed, String detail) {
        if (passed) {
            System.out.println("  [PASS] " + name);
        } else {
            System.out.println("  [FAIL] " + name + " -> " + detail);
            failures.add(name + " -> " + detail);
        }
    }

    private static void title(String title) {
        System.out.println();
        System.out.println("=== " + title);
    }

    private static void info(String message) {
        System.out.println("  [INFO] " + message);
    }

    private static void warn(String message) {
        System.out.println("  [WARN] " + message);
    }

    private static String length(byte[] bytes) {
        return bytes == null ? "null" : bytes.length + " bytes";
    }

    private static class CaseResult {
        private volatile boolean done;
        private volatile boolean handshaked;
        private volatile boolean loggedIn;
        private volatile String username;
        private volatile String error;
        private volatile long elapsed;
        private volatile byte[] proof;
        private volatile byte[] clientKey;
        private volatile byte[] encryptedSessionKey;
        private volatile byte[] sessionKey;

        private String describe() {
            return "handshaked=" + handshaked
                + ", loggedIn=" + loggedIn
                + ", username=" + username
                + ", elapsed=" + elapsed + "ms"
                + (error != null ? ", error=" + error : "");
        }
    }

    private static class HandshakeProof {
        private int version;
        private byte[] signature;
        private final List<X509Certificate> certificates = new ArrayList<>();

        private static HandshakeProof parse(byte[] bytes) throws Exception {
            HandshakeProof proof = new HandshakeProof();
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
            proof.version = in.readUnsignedByte();
            proof.signature = readField(in);
            int count = in.readUnsignedByte();
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            for (int i = 0; i < count; ++i) {
                proof.certificates.add(
                    (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(readField(in)))
                );
            }
            if (in.available() != 0) {
                throw new IllegalStateException("trailing bytes in proof: " + in.available());
            }
            return proof;
        }

        private static byte[] readField(DataInputStream in) throws Exception {
            byte[] value = new byte[in.readUnsignedShort()];
            in.readFully(value);
            return value;
        }
    }
}
