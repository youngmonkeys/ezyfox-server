package com.tvd12.ezyfoxserver.nio.testing.socket;

import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;

public abstract class EzySSLContextSpiForTest extends SSLContextSpi {

    @Override
    public abstract SSLEngine engineCreateSSLEngine();
}
