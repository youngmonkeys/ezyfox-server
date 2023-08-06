package com.tvd12.ezyfoxserver.ssl;

public class EzySimpleSslContextFactoryBuilder
    implements EzySslContextFactoryBuilder {

    @Override
    public EzySslContextFactory build() {
        return new EzySimpleSslContextFactory();
    }
}
