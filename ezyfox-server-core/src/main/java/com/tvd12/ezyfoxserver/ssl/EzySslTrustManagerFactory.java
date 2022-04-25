package com.tvd12.ezyfoxserver.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class EzySslTrustManagerFactory extends TrustManagerFactorySpi {

    private static final TrustManager TRUST_MANAGER = new X509TrustManager() {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            logger.debug("ssl: get accepted issuers");
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            logger.debug("ssl: check client trusted, chain = {}, authType = {}", chain, authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            logger.debug("ssl: check server trusted, chain = {}, authType = {}", chain, authType);
        }
    };
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        logger.debug("ssl: engine get trust managers");
        return new TrustManager[]{TRUST_MANAGER};
    }

    @Override
    protected void engineInit(KeyStore keystore) {
        logger.debug("ssl: engine init, keystore = {}", keystore);
    }

    @Override
    protected void engineInit(
        ManagerFactoryParameters managerFactoryParameters
    ) {
        logger.debug("ssl: engine init, parameters = {}", managerFactoryParameters);
    }
}
