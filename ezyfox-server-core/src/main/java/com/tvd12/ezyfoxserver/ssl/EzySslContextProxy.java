package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.function.EzyExceptionApply;
import com.tvd12.ezyfox.util.EzyLoggable;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EzySslContextProxy extends EzyLoggable {

    private final Supplier<SSLContext> sslContextSupplier;
    private final List<EzyExceptionApply<SSLContext>> sslContextReloadListeners;

    public EzySslContextProxy(
        Supplier<SSLContext> sslContextSupplier
    ) {
        this.sslContextSupplier = sslContextSupplier;
        this.sslContextReloadListeners = new ArrayList<>();
    }

    public SSLContext loadSslContext() {
        return sslContextSupplier.get();
    }

    public void onSslContextReload(
        EzyExceptionApply<SSLContext> listener
    ) {
        this.sslContextReloadListeners.add(listener);
    }

    public void reloadSsl() {
        SSLContext newSslContext = loadSslContext();
        sslContextReloadListeners.forEach(listener -> {
            try {
                listener.apply(newSslContext);
            } catch (Exception e) {
                logger.error(
                    "reload ssl context on listener: {}",
                    listener,
                    e
                );
            }
        });
    }
}
