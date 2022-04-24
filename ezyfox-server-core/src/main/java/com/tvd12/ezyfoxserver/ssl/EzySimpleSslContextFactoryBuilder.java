package com.tvd12.ezyfoxserver.ssl;

import javax.net.ssl.TrustManager;
import java.security.SecureRandom;

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
