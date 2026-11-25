package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfoxserver.ssl.EzySslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeSigner;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

public class EzySslHandshakeSignerSigningKeyTest {

    private static final String ALIAS = "test";

    @Test
    public void notKeyEntryTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.keyEntry = false;

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void notPrivateKeyTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.key = new SecretKeySpec(new byte[16], "AES");

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void nullKeyTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.key = null;

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void nullChainTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.chain = null;

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void emptyChainTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.chain = new Certificate[0];

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void leafNotX509CertificateTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.chain = new Certificate[] {mock(Certificate.class)};

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void leafExpiredTest() throws Exception {
        // given
        X509Certificate leaf = mock(X509Certificate.class);
        doThrow(new CertificateExpiredException("test")).when(leaf).checkValidity();
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.chain = new Certificate[] {leaf};

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void getKeyErrorTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.getKeyError = new UnrecoverableKeyException("test");

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void unsupportedKeyAlgorithmTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        spi.key = mock(PrivateKey.class);

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void certificateTooLargeTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        X509Certificate tooLarge = mock(X509Certificate.class);
        when(tooLarge.getEncoded()).thenReturn(new byte[EzySslHandshakeSigner.MAX_FIELD_BYTES + 1]);
        spi.chain = new Certificate[] {spi.chain[0], tooLarge};

        // when
        // then
        assertNotLoaded(spi);
    }

    @Test
    public void rsaKeyTest() throws Exception {
        // when
        // then
        assertLoaded(newRsaSpi(), 1);
    }

    @Test
    public void ecKeyTest() throws Exception {
        // when
        // then
        assertLoaded(newSpi("test-input/ssl-handshake-ec-keystore.jks"), 1);
    }

    @Test
    public void certificateChainLimitTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        Certificate[] chain = new Certificate[EzySslHandshakeSigner.MAX_CERTIFICATES + 1];
        chain[0] = spi.chain[0];
        for (int i = 1; i < chain.length; ++i) {
            X509Certificate certificate = mock(X509Certificate.class);
            when(certificate.getEncoded()).thenReturn(new byte[] {(byte) i});
            chain[i] = certificate;
        }
        spi.chain = chain;

        // when
        // then
        assertLoaded(spi, EzySslHandshakeSigner.MAX_CERTIFICATES);
    }

    @Test
    public void certificateChainAtLimitTest() throws Exception {
        // given
        FakeKeyStoreSpi spi = newRsaSpi();
        Certificate[] chain = new Certificate[EzySslHandshakeSigner.MAX_CERTIFICATES];
        chain[0] = spi.chain[0];
        for (int i = 1; i < chain.length; ++i) {
            X509Certificate certificate = mock(X509Certificate.class);
            when(certificate.getEncoded()).thenReturn(new byte[] {(byte) i});
            chain[i] = certificate;
        }
        spi.chain = chain;

        // when
        // then
        assertLoaded(spi, EzySslHandshakeSigner.MAX_CERTIFICATES);
    }

    private static void assertNotLoaded(FakeKeyStoreSpi spi) throws Exception {
        // given
        FakeKeyStoreSigner sut = new FakeKeyStoreSigner(newKeyStore(spi));

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(new byte[] {1}, new byte[] {2});

        // then
        Asserts.assertFalse(loaded);
        Asserts.assertFalse(sut.isActive());
        Asserts.assertNull(proof);
    }

    private static void assertLoaded(FakeKeyStoreSpi spi, int expectedCertificateCount) throws Exception {
        // given
        FakeKeyStoreSigner sut = new FakeKeyStoreSigner(newKeyStore(spi));
        byte[] clientKey = new byte[] {1, 2, 3};
        byte[] encryptedSessionKey = new byte[] {4, 5};

        // when
        boolean loaded = sut.reload();
        byte[] proof = sut.sign(clientKey, encryptedSessionKey);

        // then
        Asserts.assertTrue(loaded);
        Asserts.assertTrue(sut.isActive());
        Asserts.assertEquals(readCertificateCount(proof), expectedCertificateCount);
        if (expectedCertificateCount == 1) {
            EzySslHandshakeSignerTest.verifyProof(proof, clientKey, encryptedSessionKey);
        }
    }

    private static int readCertificateCount(byte[] proof) {
        ByteBuffer buffer = ByteBuffer.wrap(proof);
        buffer.get();
        int signedLength = buffer.getShort() & 0xFFFF;
        buffer.position(buffer.position() + signedLength);
        return buffer.get() & 0xFF;
    }

    private static FakeKeyStoreSpi newRsaSpi() throws Exception {
        return newSpi("test-input/ssl-handshake-keystore.jks");
    }

    private static FakeKeyStoreSpi newSpi(String keyStoreFile) throws Exception {
        char[] password = handshakePassword();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream stream = Files.newInputStream(Paths.get(keyStoreFile))) {
            keyStore.load(stream, password);
        }
        FakeKeyStoreSpi spi = new FakeKeyStoreSpi();
        spi.key = keyStore.getKey("handshake", password);
        spi.chain = keyStore.getCertificateChain("handshake");
        return spi;
    }

    private static char[] handshakePassword() throws Exception {
        return new String(
            Files.readAllBytes(Paths.get("test-input/ssl-handshake-password.txt")),
            StandardCharsets.UTF_8
        ).trim().toCharArray();
    }

    private static KeyStore newKeyStore(FakeKeyStoreSpi spi) throws Exception {
        KeyStore keyStore = new KeyStore(spi, null, "fake") {};
        keyStore.load(null, null);
        return keyStore;
    }

    private static class FakeKeyStoreSigner extends EzySslHandshakeSigner {

        private final KeyStore keyStore;

        FakeKeyStoreSigner(KeyStore keyStore) {
            super(() -> null);
            this.keyStore = keyStore;
        }

        @Override
        protected SigningKey loadSigningKey(EzySslConfig config) {
            return newSigningKey(keyStore, ALIAS, new char[0]);
        }
    }

    private static class FakeKeyStoreSpi extends KeyStoreSpi {

        private boolean keyEntry = true;
        private Key key;
        private Certificate[] chain;
        private UnrecoverableKeyException getKeyError;

        @Override
        public Key engineGetKey(String alias, char[] password) throws UnrecoverableKeyException {
            if (getKeyError != null) {
                throw getKeyError;
            }
            return key;
        }

        @Override
        public Certificate[] engineGetCertificateChain(String alias) {
            return chain;
        }

        @Override
        public Certificate engineGetCertificate(String alias) {
            return chain == null || chain.length == 0 ? null : chain[0];
        }

        @Override
        public Date engineGetCreationDate(String alias) {
            return new Date();
        }

        @Override
        public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) {}

        @Override
        public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain) {}

        @Override
        public void engineSetCertificateEntry(String alias, Certificate cert) {}

        @Override
        public void engineDeleteEntry(String alias) {}

        @Override
        public Enumeration<String> engineAliases() {
            return Collections.enumeration(Collections.singletonList(ALIAS));
        }

        @Override
        public boolean engineContainsAlias(String alias) {
            return ALIAS.equals(alias);
        }

        @Override
        public int engineSize() {
            return 1;
        }

        @Override
        public boolean engineIsKeyEntry(String alias) {
            return keyEntry;
        }

        @Override
        public boolean engineIsCertificateEntry(String alias) {
            return !keyEntry;
        }

        @Override
        public String engineGetCertificateAlias(Certificate cert) {
            return ALIAS;
        }

        @Override
        public void engineStore(OutputStream stream, char[] password) {}

        @Override
        public void engineLoad(InputStream stream, char[] password) {}
    }
}
