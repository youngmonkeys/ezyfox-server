package com.tvd12.ezyfoxserver.nio.builder.impl;

import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class EzyWebSocketSecureServerCreator
    extends EzyWebSocketServerCreator {

    protected final EzySslContextProxy sslContextProxy;

    public EzyWebSocketSecureServerCreator(
        EzySslContextProxy sslContextProxy
    ) {
        this.sslContextProxy = sslContextProxy;
    }

    @Override
    protected void configServer(
        Server server,
        HttpConfiguration httpConfig,
        ServerConnector wsConnector
    ) {

        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setSslContext(sslContextProxy.loadSslContext());
        sslContextProxy.onSslContextReload(sslContext ->
            sslContextFactory.reload(factory ->
                factory.setSslContext(sslContext)
            )
        );

        ServerConnector wssConnector = new ServerConnector(
            server,
            new SslConnectionFactory(
                sslContextFactory,
                HttpVersion.HTTP_1_1.asString()
            ),
            new HttpConnectionFactory(httpsConfig)
        );

        wssConnector.setHost(setting.getAddress());
        wssConnector.setPort(setting.getSslPort());

        server.addConnector(wssConnector);
    }
}
