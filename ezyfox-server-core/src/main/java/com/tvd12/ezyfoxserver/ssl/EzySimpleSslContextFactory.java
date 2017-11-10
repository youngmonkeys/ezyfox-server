package com.tvd12.ezyfoxserver.ssl;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.tvd12.ezyfoxserver.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfoxserver.stream.EzyInputStreamLoader;
import com.tvd12.ezyfoxserver.stream.EzyInputStreamReader;
import com.tvd12.ezyfoxserver.stream.EzySimpleInputStreamReader;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
public class EzySimpleSslContextFactory 
        extends EzyLoggable 
        implements EzySslContextFactory {

    protected SecureRandom secureRandom;
    protected TrustManager[] trustManagers;
    
    private static final String SUNX509             = "SunX509";
    private static final String PROTOCOL            = "TLS";
    private static final String JKS_KEYSTORE        = "JKS";
    private static final String ALGORITHM_PROPERTY  = "ssl.KeyManagerFactory.algorithm";
    
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
        context.init(keyManagerFactory.getKeyManagers(), trustManagers, secureRandom);
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
        keyStore.load(stream, password);
    }
    
    protected char[] getPassword(String file) {
        InputStream stream = newInputStreamLoader().load(file);
        return newInputStreamReader().readChars(stream, "UTF-8");
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
        String algorithm = Security.getProperty(getAlgorithmProperty());
        return algorithm != null ? algorithm : SUNX509;
    }
    
    protected String getAlgorithmProperty() {
        return ALGORITHM_PROPERTY;
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
