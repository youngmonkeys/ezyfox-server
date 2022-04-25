package com.tvd12.ezyfoxserver.ssl;

import javax.net.ssl.SSLContext;

public interface EzySslContextFactory {

    SSLContext newSslContext(EzySslConfig config) throws Exception;
}
