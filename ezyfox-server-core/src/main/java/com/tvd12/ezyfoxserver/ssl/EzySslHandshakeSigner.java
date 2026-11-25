package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfox.stream.EzyInputStreamLoader;
import com.tvd12.ezyfox.stream.EzySimpleInputStreamReader;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class EzySslHandshakeSigner extends EzyLoggable {

    public static final byte PROOF_VERSION = 1;
    public static final String SIGNATURE_DOMAIN = "ezyfox-handshake-v1";
    public static final int MAX_FIELD_BYTES = 0xFFFF;
    public static final int MAX_CERTIFICATES = 8;
    protected static final String JKS_KEYSTORE = "JKS";

    protected final Supplier<EzySslConfig> sslConfigSupplier;
    protected volatile SigningKey signingKey;

    public EzySslHandshakeSigner(Supplier<EzySslConfig> sslConfigSupplier) {
        this.sslConfigSupplier = sslConfigSupplier;
    }

    public boolean reload() {
        try {
            SigningKey key = loadSigningKey(sslConfigSupplier.get());
            if (key == null) {
                logger.warn("ssl handshake signer: no usable RSA or EC key, socket handshakes are not signed");
                return false;
            }
            signingKey = key;
            logger.info(
                "ssl handshake signer: sign socket handshakes with {} ({} bits), certificate: {}, expires: {}",
                key.algorithm,
                key.keyBits,
                key.subject,
                new Date(key.notAfter)
            );
            return true;
        } catch (Throwable e) {
            logger.info("ssl handshake signer: cannot load the key store, socket handshakes are not signed: {}", e.toString());
            return false;
        }
    }

    public boolean isActive() {
        SigningKey key = signingKey;
        return key != null && !isExpired(key);
    }

    public byte[] sign(byte[] clientKey, byte[] encryptedSessionKey) throws Exception {
        SigningKey key = signingKey;
        if (key == null || isExpired(key)) {
            return null;
        }
        Signature signature = Signature.getInstance(key.algorithm);
        signature.initSign(key.privateKey);
        signature.update(signedMessage(clientKey, encryptedSessionKey));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(PROOF_VERSION);
        writeField(out, signature.sign());
        out.write(key.certificates.size());
        for (byte[] certificate : key.certificates) {
            writeField(out, certificate);
        }
        return out.toByteArray();
    }

    public static byte[] signedMessage(byte[] clientKey, byte[] encryptedSessionKey) {
        byte[] domain = SIGNATURE_DOMAIN.getBytes(StandardCharsets.US_ASCII);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(domain, 0, domain.length);
        writeField(out, clientKey);
        writeField(out, encryptedSessionKey);
        return out.toByteArray();
    }

    protected static void writeField(ByteArrayOutputStream out, byte[] value) {
        if (value.length > MAX_FIELD_BYTES) {
            throw new IllegalArgumentException("field too large: " + value.length);
        }
        out.write(value.length >>> 8);
        out.write(value.length & 0xFF);
        out.write(value, 0, value.length);
    }

    protected SigningKey loadSigningKey(EzySslConfig config) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(JKS_KEYSTORE);
        try (InputStream stream = newInputStreamLoader().load(config.getKeyStoreFile())) {
            keyStore.load(stream, readPassword(config.getKeyStorePasswordFile()));
        }
        char[] keyPassword = readPassword(config.getCertificatePasswordFile());
        SigningKey best = null;
        for (String alias : Collections.list(keyStore.aliases())) {
            SigningKey candidate = newSigningKey(keyStore, alias, keyPassword);
            if (candidate != null && (best == null || candidate.notAfter > best.notAfter)) {
                best = candidate;
            }
        }
        return best;
    }

    protected SigningKey newSigningKey(KeyStore keyStore, String alias, char[] keyPassword) {
        try {
            if (!keyStore.isKeyEntry(alias)) {
                return null;
            }
            Key key = keyStore.getKey(alias, keyPassword);
            Certificate[] chain = keyStore.getCertificateChain(alias);
            if (!(key instanceof PrivateKey)
                || chain == null
                || chain.length == 0
                || !(chain[0] instanceof X509Certificate)
            ) {
                return null;
            }
            X509Certificate leaf = (X509Certificate) chain[0];
            leaf.checkValidity();
            String algorithm;
            int keyBits;
            if (key instanceof RSAKey) {
                algorithm = "SHA256withRSA";
                keyBits = ((RSAKey) key).getModulus().bitLength();
            } else if (key instanceof ECKey) {
                algorithm = "SHA256withECDSA";
                keyBits = ((ECKey) key).getParams().getOrder().bitLength();
            } else {
                return null;
            }
            List<byte[]> certificates = new ArrayList<>();
            for (int i = 0; i < chain.length && i < MAX_CERTIFICATES; ++i) {
                byte[] encoded = chain[i].getEncoded();
                if (encoded.length > MAX_FIELD_BYTES) {
                    return null;
                }
                certificates.add(encoded);
            }
            return new SigningKey(
                (PrivateKey) key,
                algorithm,
                keyBits,
                certificates,
                leaf.getSubjectX500Principal().getName(),
                leaf.getNotAfter().getTime()
            );
        } catch (Exception e) {
            logger.info("ssl handshake signer: skip key store entry: {}, error: {}", alias, e.toString());
            return null;
        }
    }

    protected boolean isExpired(SigningKey key) {
        return currentTimeMillis() > key.notAfter;
    }

    protected long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    protected char[] readPassword(String file) throws Exception {
        try (InputStream stream = newInputStreamLoader().load(file)) {
            return EzySimpleInputStreamReader.builder()
                .build()
                .readString(stream, EzyStrings.UTF_8)
                .trim()
                .toCharArray();
        }
    }

    protected EzyInputStreamLoader newInputStreamLoader() {
        return EzyAnywayInputStreamLoader.builder()
            .context(getClass())
            .build();
    }

    protected static class SigningKey {
        protected final PrivateKey privateKey;
        protected final String algorithm;
        protected final int keyBits;
        protected final List<byte[]> certificates;
        protected final String subject;
        protected final long notAfter;

        protected SigningKey(
            PrivateKey privateKey,
            String algorithm,
            int keyBits,
            List<byte[]> certificates,
            String subject,
            long notAfter
        ) {
            this.privateKey = privateKey;
            this.algorithm = algorithm;
            this.keyBits = keyBits;
            this.certificates = certificates;
            this.subject = subject;
            this.notAfter = notAfter;
        }
    }
}
