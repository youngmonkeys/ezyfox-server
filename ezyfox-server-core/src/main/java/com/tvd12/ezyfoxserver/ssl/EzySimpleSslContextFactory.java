package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfox.stream.EzyInputStreamLoader;
import com.tvd12.ezyfox.stream.EzyInputStreamReader;
import com.tvd12.ezyfox.stream.EzySimpleInputStreamReader;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Setter;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

@Setter
public class EzySimpleSslContextFactory
    extends EzyLoggable
    implements EzySslContextFactory {

    protected static final String SUNX509 = "SunX509";
    protected static final String PROTOCOL = "TLS";
    protected static final String JKS_KEYSTORE = "JKS";

    @Override
    public SSLContext newSslContext(EzySslConfig config) throws Exception {
        InputStream keyStoreStream = loadKeyStoreStream(config.getKeyStoreFile());
        char[] keyStorePassword = getPassword(config.getKeyStorePasswordFile());
        char[] certificatePassword = getPassword(config.getCertificatePasswordFile());
        KeyStore keyStore = newKeyStore(config);
        loadKeyStore(keyStore, keyStoreStream, keyStorePassword);

        // Set up key manager factory to use our key store
        KeyManagerFactory keyManagerFactory = newKeyManagerFactory(config);
        initKeyManagerFactory(keyManagerFactory, keyStore, certificatePassword);

        // Set up trust manager factory to use our key store
        TrustManagerFactory trustManagerFactory = newTrustManagerFactory(config);
        initTrustManagerFactory(trustManagerFactory, keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        // Initialize the SSLContext to work with our key managers.
        SSLContext context = SSLContext.getInstance(getProtocol());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        context.init(keyManagers, trustManagers, null);
        return context;
    }

    protected void initKeyManagerFactory(
        KeyManagerFactory factory,
        KeyStore keyStore,
        char[] password
    ) throws Exception {
        factory.init(keyStore, password);
    }

    protected KeyManagerFactory newKeyManagerFactory(
        EzySslConfig config
    ) throws Exception {
        return KeyManagerFactory.getInstance(getAlgorithm(config));
    }

    protected void initTrustManagerFactory(
        TrustManagerFactory factory,
        KeyStore keyStore
    ) throws Exception {
        factory.init(keyStore);
    }

    protected TrustManagerFactory newTrustManagerFactory(
        EzySslConfig config
    ) throws Exception {
        return TrustManagerFactory.getInstance(getAlgorithm(config));
    }

    protected void loadKeyStore(
        KeyStore keyStore,
        InputStream stream,
        char[] password
    ) throws Exception {
        try {
            keyStore.load(stream, password);
        } finally {
            stream.close();
        }
    }

    protected char[] getPassword(String file) throws Exception {
        InputStream stream = newInputStreamLoader().load(file);
        char[] answer;
        try {
            answer = newInputStreamReader()
                .readString(stream, EzyStrings.UTF_8)
                .trim()
                .toCharArray();
        } finally {
            stream.close();
        }
        return answer;
    }

    protected InputStream loadKeyStoreStream(String file) {
        return newInputStreamLoader().load(file);
    }

    protected KeyStore newKeyStore(
        @SuppressWarnings("unused") EzySslConfig config
    ) throws KeyStoreException {
        return KeyStore.getInstance(getKeyStoreType());
    }

    protected String getKeyStoreType() {
        return JKS_KEYSTORE;
    }

    protected String getProtocol() {
        return PROTOCOL;
    }

    protected String getAlgorithm(EzySslConfig config) {
        String algorithm = getDefaultAlgorithm();
        return algorithm != null ? algorithm : SUNX509;
    }

    protected String getDefaultAlgorithm() {
        return KeyManagerFactory.getDefaultAlgorithm();
    }

    protected EzyInputStreamLoader newInputStreamLoader() {
        return EzyAnywayInputStreamLoader.builder()
            .context(getClass())
            .build();
    }

    protected EzyInputStreamReader newInputStreamReader() {
        return EzySimpleInputStreamReader.builder()
            .build();
    }
}
