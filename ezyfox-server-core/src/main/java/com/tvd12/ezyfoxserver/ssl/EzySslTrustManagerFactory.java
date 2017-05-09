package com.tvd12.ezyfoxserver.ssl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EzySslTrustManagerFactory extends TrustManagerFactorySpi {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final TrustManager TRUST_MANAGER = new X509TrustManager() {
        
        private Logger logger = LoggerFactory.getLogger(getClass());
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            getLogger().debug("ssl: get accepted issuers");
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
            getLogger().debug("ssl: check client trusted, chain = {}, authType = {}", chain, authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
            getLogger().debug("ssl: check server trusted, chain = {}, authType = {}", chain, authType);
        }
        
        protected Logger getLogger() {
            return logger;
        }
    };

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        getLogger().debug("ssl: engine get trust managers");
        return new TrustManager[]{TRUST_MANAGER};
    }

    @Override
    protected void engineInit(KeyStore keystore)
        throws KeyStoreException {
        getLogger().debug("ssl: engine init, keystore = {}", keystore);
    }

    @Override
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters)
        throws InvalidAlgorithmParameterException {
        getLogger().debug("ssl: engine init, parameters = {}", managerFactoryParameters);
    }
    
    protected Logger getLogger() {
        return logger;
    }
}
