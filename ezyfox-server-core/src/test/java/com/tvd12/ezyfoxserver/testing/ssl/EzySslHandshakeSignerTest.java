package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeSigner;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EzySslHandshakeSignerTest extends BaseCoreTest {

    public static EzySimpleSslConfig newSslConfig() {
        EzySimpleSslConfig sslConfig = new EzySimpleSslConfig();
        sslConfig.setKeyStoreFile("test-input/ssl-keystore.txt");
        sslConfig.setKeyStorePasswordFile("test-input/ssl-keystore-password.txt");
        sslConfig.setCertificatePasswordFile("test-input/ssl-certificate-password.txt");
        return sslConfig;
    }

    public static void verifyProof(
        byte[] proof,
        byte[] clientKey,
        byte[] encryptedSessionKey
    ) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(proof);
        Asserts.assertEquals(buffer.get(), EzySslHandshakeSigner.PROOF_VERSION);
        byte[] signed = readField(buffer);
        int count = buffer.get() & 0xFF;
        List<X509Certificate> certificates = new ArrayList<>();
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        for (int i = 0; i < count; ++i) {
            certificates.add(
                (X509Certificate) factory.generateCertificate(
                    new ByteArrayInputStream(readField(buffer))
                )
            );
        }
        Asserts.assertFalse(buffer.hasRemaining());
        Asserts.assertTrue(count > 0);
        X509Certificate leaf = certificates.get(0);
        Signature signature = Signature.getInstance(
            "RSA".equals(leaf.getPublicKey().getAlgorithm()) ? "SHA256withRSA" : "SHA256withECDSA"
        );
        signature.initVerify(leaf.getPublicKey());
        signature.update(EzySslHandshakeSigner.signedMessage(clientKey, encryptedSessionKey));
        Asserts.assertTrue(signature.verify(signed));
    }

    private static byte[] readField(ByteBuffer buffer) {
        int length = buffer.getShort() & 0xFFFF;
        byte[] answer = new byte[length];
        buffer.get(answer);
        return answer;
    }

    @Test
    public void signTest() throws Exception {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(EzySslHandshakeSignerTest::newSslConfig);
        byte[] clientKey = new byte[] {1, 2, 3};
        byte[] encryptedSessionKey = new byte[] {4, 5};

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(clientKey, encryptedSessionKey);

        // then
        Asserts.assertTrue(loaded);
        Asserts.assertTrue(sut.isActive());
        verifyProof(proof, clientKey, encryptedSessionKey);
    }

    @Test
    public void signFixedVectorTest() throws Exception {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(() -> {
            EzySimpleSslConfig sslConfig = new EzySimpleSslConfig();
            sslConfig.setKeyStoreFile("test-input/ssl-handshake-keystore.jks");
            sslConfig.setKeyStorePasswordFile("test-input/ssl-handshake-password.txt");
            sslConfig.setCertificatePasswordFile("test-input/ssl-handshake-password.txt");
            return sslConfig;
        });
        byte[] clientKey = byteRange(0x01, 0x21);
        byte[] encryptedSessionKey = byteRange(0xA0, 0xC0);
        String expectedProof = new String(
            Files.readAllBytes(Paths.get("test-input/ssl-handshake-proof.txt")),
            StandardCharsets.US_ASCII
        ).trim();

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(clientKey, encryptedSessionKey);

        // then
        Asserts.assertTrue(loaded);
        Asserts.assertEquals(toHex(proof), expectedProof);
        verifyProof(proof, clientKey, encryptedSessionKey);
    }

    private static byte[] byteRange(int from, int to) {
        byte[] answer = new byte[to - from];
        for (int i = 0; i < answer.length; ++i) {
            answer[i] = (byte) (from + i);
        }
        return answer;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b & 0xFF));
        }
        return builder.toString();
    }

    @Test
    public void signWrongMessageTest() throws Exception {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(EzySslHandshakeSignerTest::newSslConfig);
        sut.reload();
        byte[] proof = sut.sign(new byte[] {1, 2, 3}, new byte[] {4, 5});

        // when
        Throwable e = Asserts.assertThrows(() -> verifyProof(proof, new byte[] {9, 2, 3}, new byte[] {4, 5}));

        // then
        Asserts.assertEqualsType(e, AssertionError.class);
    }

    @Test
    public void signedMessageTest() {
        // given
        byte[] domain = EzySslHandshakeSigner.SIGNATURE_DOMAIN.getBytes(StandardCharsets.US_ASCII);
        byte[] expected = Arrays.copyOf(domain, domain.length + 7);
        System.arraycopy(new byte[] {0, 2, 'c', 'k', 0, 1, 'e'}, 0, expected, domain.length, 7);

        // when
        byte[] actual = EzySslHandshakeSigner.signedMessage(
            new byte[] {'c', 'k'},
            new byte[] {'e'}
        );

        // then
        Asserts.assertEquals(actual, expected);
    }

    @Test
    public void signedMessageTooLargeTest() {
        // given
        byte[] tooLarge = new byte[EzySslHandshakeSigner.MAX_FIELD_BYTES + 1];

        // when
        Throwable e = Asserts.assertThrows(() -> EzySslHandshakeSigner.signedMessage(tooLarge, new byte[1]));

        // then
        Asserts.assertEqualsType(e, IllegalArgumentException.class);
    }

    @Test
    public void notLoadedTest() throws Exception {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(EzySslHandshakeSignerTest::newSslConfig);

        // when
        byte[] proof = sut.sign(new byte[] {1}, new byte[] {2});

        // then
        Asserts.assertFalse(sut.isActive());
        Asserts.assertNull(proof);
    }

    @Test
    public void reloadMissingKeyStoreTest() {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(() -> {
            EzySimpleSslConfig sslConfig = newSslConfig();
            sslConfig.setKeyStoreFile("test-input/not-found-keystore.txt");
            return sslConfig;
        });

        // when
        boolean loaded = sut.reload();

        // then
        Asserts.assertFalse(loaded);
        Asserts.assertFalse(sut.isActive());
    }

    @Test
    public void expiredKeyTest() throws Exception {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(EzySslHandshakeSignerTest::newSslConfig) {
            @Override
            protected long currentTimeMillis() {
                return Long.MAX_VALUE;
            }
        };

        // when
        sut.reload();
        byte[] proof = sut.sign(new byte[] {1}, new byte[] {2});

        // then
        Asserts.assertFalse(sut.isActive());
        Asserts.assertNull(proof);
    }

    @Test
    public void reloadConfigErrorTest() {
        // given
        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(() -> {
            throw new IllegalStateException("test");
        });

        // when
        boolean loaded = sut.reload();

        // then
        Asserts.assertFalse(loaded);
    }

    @Test
    public void reloadChooseLatestExpiredKeyTest() throws Exception {
        // given
        KeyStore rsaKeyStore = loadKeyStore("test-input/ssl-handshake-keystore.jks");
        KeyStore ecKeyStore = loadKeyStore("test-input/ssl-handshake-ec-keystore.jks");
        X509Certificate rsaCertificate = (X509Certificate) rsaKeyStore.getCertificate("handshake");
        X509Certificate ecCertificate = (X509Certificate) ecKeyStore.getCertificate("handshake");
        Asserts.assertTrue(ecCertificate.getNotAfter().after(rsaCertificate.getNotAfter()));

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("trusted", rsaCertificate);
        copyKeyEntry(rsaKeyStore, keyStore, "rsa");
        copyKeyEntry(ecKeyStore, keyStore, "ec");
        copyKeyEntry(rsaKeyStore, keyStore, "rsa-copy");
        Path keyStoreFile = storeKeyStore(keyStore);

        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(() -> newHandshakeSslConfig(keyStoreFile));
        byte[] clientKey = new byte[] {1, 2, 3};
        byte[] encryptedSessionKey = new byte[] {4, 5};

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(clientKey, encryptedSessionKey);

        // then
        Asserts.assertTrue(loaded);
        Asserts.assertTrue(sut.isActive());
        Asserts.assertEquals(readLeafCertificate(proof), ecCertificate.getEncoded());
        verifyProof(proof, clientKey, encryptedSessionKey);
        Files.deleteIfExists(keyStoreFile);
    }

    @Test
    public void reloadNoKeyEntryTest() throws Exception {
        // given
        KeyStore rsaKeyStore = loadKeyStore("test-input/ssl-handshake-keystore.jks");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("trusted", rsaKeyStore.getCertificate("handshake"));
        Path keyStoreFile = storeKeyStore(keyStore);

        EzySslHandshakeSigner sut = new EzySslHandshakeSigner(() -> newHandshakeSslConfig(keyStoreFile));

        // when
        boolean loaded = sut.reload();

        // then
        Asserts.assertFalse(loaded);
        Asserts.assertFalse(sut.isActive());
        Files.deleteIfExists(keyStoreFile);
    }

    private static EzySimpleSslConfig newHandshakeSslConfig(Path keyStoreFile) {
        EzySimpleSslConfig sslConfig = new EzySimpleSslConfig();
        sslConfig.setKeyStoreFile(keyStoreFile.toString());
        sslConfig.setKeyStorePasswordFile("test-input/ssl-handshake-password.txt");
        sslConfig.setCertificatePasswordFile("test-input/ssl-handshake-password.txt");
        return sslConfig;
    }

    private static char[] handshakePassword() throws Exception {
        return new String(
            Files.readAllBytes(Paths.get("test-input/ssl-handshake-password.txt")),
            StandardCharsets.UTF_8
        ).trim().toCharArray();
    }

    private static KeyStore loadKeyStore(String file) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream stream = Files.newInputStream(Paths.get(file))) {
            keyStore.load(stream, handshakePassword());
        }
        return keyStore;
    }

    private static void copyKeyEntry(KeyStore from, KeyStore to, String alias) throws Exception {
        char[] password = handshakePassword();
        Key key = from.getKey("handshake", password);
        Certificate[] chain = from.getCertificateChain("handshake");
        to.setKeyEntry(alias, key, password, chain);
    }

    private static Path storeKeyStore(KeyStore keyStore) throws Exception {
        Path file = Files.createTempFile("ssl-handshake-keystore", ".jks");
        try (OutputStream stream = Files.newOutputStream(file)) {
            keyStore.store(stream, handshakePassword());
        }
        return file;
    }

    private static byte[] readLeafCertificate(byte[] proof) {
        ByteBuffer buffer = ByteBuffer.wrap(proof);
        buffer.get();
        readField(buffer);
        buffer.get();
        return readField(buffer);
    }

    @Test
    public void reloadCandidateOrderTest() throws Exception {
        // given
        KeyStore rsaKeyStore = loadKeyStore("test-input/ssl-handshake-keystore.jks");
        KeyStore ecKeyStore = loadKeyStore("test-input/ssl-handshake-ec-keystore.jks");
        X509Certificate ecCertificate = (X509Certificate) ecKeyStore.getCertificate("handshake");
        Path keyStoreFile = storeKeyStore(newMultiAliasKeyStore(rsaKeyStore, 4));
        CandidateSequenceSigner sut = new CandidateSequenceSigner(
            keyStoreFile,
            Arrays.asList(null, rsaKeyStore, ecKeyStore, rsaKeyStore)
        );

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(new byte[] {1}, new byte[] {2});

        // then
        Asserts.assertTrue(loaded);
        Asserts.assertEquals(sut.callCount, 4);
        Asserts.assertEquals(readLeafCertificate(proof), ecCertificate.getEncoded());
        Files.deleteIfExists(keyStoreFile);
    }

    @Test
    public void reloadKeepFirstLatestCandidateTest() throws Exception {
        // given
        KeyStore rsaKeyStore = loadKeyStore("test-input/ssl-handshake-keystore.jks");
        KeyStore ecKeyStore = loadKeyStore("test-input/ssl-handshake-ec-keystore.jks");
        X509Certificate ecCertificate = (X509Certificate) ecKeyStore.getCertificate("handshake");
        Path keyStoreFile = storeKeyStore(newMultiAliasKeyStore(rsaKeyStore, 3));
        CandidateSequenceSigner sut = new CandidateSequenceSigner(
            keyStoreFile,
            Arrays.asList(ecKeyStore, rsaKeyStore, null)
        );

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(new byte[] {1}, new byte[] {2});

        // then
        Asserts.assertTrue(loaded);
        Asserts.assertEquals(sut.callCount, 3);
        Asserts.assertEquals(readLeafCertificate(proof), ecCertificate.getEncoded());
        Files.deleteIfExists(keyStoreFile);
    }

    @Test
    public void reloadAllCandidatesNullTest() throws Exception {
        // given
        KeyStore rsaKeyStore = loadKeyStore("test-input/ssl-handshake-keystore.jks");
        Path keyStoreFile = storeKeyStore(newMultiAliasKeyStore(rsaKeyStore, 2));
        CandidateSequenceSigner sut = new CandidateSequenceSigner(
            keyStoreFile,
            Arrays.asList(null, null)
        );

        // when
        boolean loaded = sut.reload();

        // then
        Asserts.assertFalse(loaded);
        Asserts.assertFalse(sut.isActive());
        Asserts.assertEquals(sut.callCount, 2);
        Files.deleteIfExists(keyStoreFile);
    }

    @Test
    public void reloadEmptyKeyStoreTest() throws Exception {
        // given
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        Path keyStoreFile = storeKeyStore(keyStore);
        CandidateSequenceSigner sut = new CandidateSequenceSigner(
            keyStoreFile,
            Collections.emptyList()
        );

        // when
        boolean loaded = sut.reload();

        // then
        Asserts.assertFalse(loaded);
        Asserts.assertEquals(sut.callCount, 0);
        Files.deleteIfExists(keyStoreFile);
    }

    private static KeyStore newMultiAliasKeyStore(KeyStore source, int aliasCount) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        for (int i = 0; i < aliasCount; ++i) {
            copyKeyEntry(source, keyStore, "alias-" + i);
        }
        return keyStore;
    }

    private static class CandidateSequenceSigner extends EzySslHandshakeSigner {

        private final List<KeyStore> candidateSources;
        private int callCount;

        CandidateSequenceSigner(Path keyStoreFile, List<KeyStore> candidateSources) {
            super(() -> newHandshakeSslConfig(keyStoreFile));
            this.candidateSources = candidateSources;
        }

        @Override
        protected SigningKey newSigningKey(KeyStore keyStore, String alias, char[] keyPassword) {
            KeyStore source = candidateSources.get(callCount++);
            return source == null
                ? null
                : super.newSigningKey(source, "handshake", keyPassword);
        }
    }
}
