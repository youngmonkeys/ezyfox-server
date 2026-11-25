package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.function.EzyExceptionApply;
import com.tvd12.ezyfox.util.EzyLoggable;

import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class EzySslContextProxy extends EzyLoggable {

    private final Supplier<SSLContext> sslContextSupplier;
    private final List<EzyExceptionApply<SSLContext>> sslContextReloadListeners;
    private volatile SSLContext sslContext;

    public EzySslContextProxy(
        Supplier<SSLContext> sslContextSupplier
    ) {
        this.sslContextSupplier = sslContextSupplier;
        this.sslContextReloadListeners = new CopyOnWriteArrayList<>();
    }

    public SSLContext loadSslContext() {
        SSLContext answer = sslContext;
        if (answer == null) {
            synchronized (this) {
                answer = sslContext;
                if (answer == null) {
                    answer = sslContextSupplier.get();
                    sslContext = answer;
                }
            }
        }
        return answer;
    }

    public void onSslContextReload(
        EzyExceptionApply<SSLContext> listener
    ) {
        this.sslContextReloadListeners.add(listener);
    }

    public void reloadSsl() {
        SSLContext newSslContext = sslContextSupplier.get();
        synchronized (this) {
            sslContext = newSslContext;
        }
        sslContextReloadListeners.forEach(listener -> {
            try {
                logger.info("start apply reload ssl for: {}", listener);
                listener.apply(newSslContext);
                logger.info("finish apply reload ssl for: {}", listener);
            } catch (Exception e) {
                logger.error(
                    "apply reload ssl context on listener: {} error",
                    listener,
                    e
                );
            }
        });
    }
}
