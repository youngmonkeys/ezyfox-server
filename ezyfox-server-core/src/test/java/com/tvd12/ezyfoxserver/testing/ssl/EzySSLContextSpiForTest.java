package com.tvd12.ezyfoxserver.testing.ssl;

import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;

public abstract class EzySSLContextSpiForTest extends SSLContextSpi {

    @Override
    public abstract SSLEngine engineCreateSSLEngine();
}
