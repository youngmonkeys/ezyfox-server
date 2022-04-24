package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfox.stream.EzyInputStreamLoader;
import com.tvd12.ezyfox.stream.EzyInputStreamReader;
import com.tvd12.ezyfox.stream.EzySimpleInputStreamReader;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Setter;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;

@Setter
public class EzySimpleSslContextFactory
    extends EzyLoggable
    implements EzySslContextFactory {

    protected static final String SUNX509 = "SunX509";
    protected static final String PROTOCOL = "TLS";
    protected static final String JKS_KEYSTORE = "JKS";
    protected SecureRandom secureRandom;
    protected TrustManager[] trustManagers;

    @Override
    public SSLContext newSslContext(EzySslConfig config) throws Exception {
        return tryNewSslContext(config);
    }

    protected SSLContext tryNewSslContext(EzySslConfig config) throws Exception {
        InputStream keyStoreStream = loadKeyStoreStream(config.getKeyStoreFile());
        char[] keyStorePassword = getPassword(config.getKeyStorePasswordFile());
        char[] certificatePassword = getPassword(config.getCertificatePasswordFile());
        KeyStore keyStore = newKeyStore(config);
        loadKeyStore(keyStore, keyStoreStream, keyStorePassword);

        // Set up key manager factory to use our key store
        KeyManagerFactory keyManagerFactory = newKeyManagerFactory(config);
        initKeyManagerFactory(keyManagerFactory, keyStore, certificatePassword);

        // Initialize the SSLContext to work with our key managers.
        SSLContext context = SSLContext.getInstance(getProtocol());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        context.init(keyManagers, null, null);
        return context;
    }

    protected void initKeyManagerFactory(
        KeyManagerFactory factory, KeyStore keyStore, char[] password)
        throws Exception {
        factory.init(keyStore, password);
    }

    protected KeyManagerFactory newKeyManagerFactory(EzySslConfig config)
        throws Exception {
        return KeyManagerFactory.getInstance(getAlgorithm(config));
    }

    protected void loadKeyStore(KeyStore keyStore, InputStream stream, char[] password)
        throws Exception {
        try {
            keyStore.load(stream, password);
        } finally {
            stream.close();
        }
    }

    protected char[] getPassword(String file) throws Exception {
        InputStream stream = newInputStreamLoader().load(file);
        char[] answer = null;
        try {
            answer = newInputStreamReader().readChars(stream, "UTF-8");
        } finally {
            stream.close();
        }
        return answer;
    }

    protected InputStream loadKeyStoreStream(String file) {
        return newInputStreamLoader().load(file);
    }

    protected KeyStore newKeyStore(EzySslConfig config) throws KeyStoreException {
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
