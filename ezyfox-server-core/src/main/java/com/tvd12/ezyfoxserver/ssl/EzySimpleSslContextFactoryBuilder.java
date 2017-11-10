package com.tvd12.ezyfoxserver.ssl;

import java.security.SecureRandom;

import javax.net.ssl.TrustManager;

public class EzySimpleSslContextFactoryBuilder 
        implements EzySslContextFactoryBuilder {

    @Override
    public EzySslContextFactory build() {
        EzySimpleSslContextFactory factory = new EzySimpleSslContextFactory();
        factory.setSecureRandom(getSecureRandom());
        factory.setTrustManagers(getTrustManagers());
        return factory;
    }
    
    protected TrustManager[] getTrustManagers() {
        return new EzySslTrustManagerFactory().engineGetTrustManagers();
    }
    
    protected SecureRandom getSecureRandom() {
        return new SecureRandom();
    }
    
}
