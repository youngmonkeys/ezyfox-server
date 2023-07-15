package com.tvd12.ezyfoxserver.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;

public final class EzySslEngines {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        EzySslEngines.class
    );

    private EzySslEngines() {}

    public static void safeCloseOutbound(SSLEngine sslEngine) {
        try {
            sslEngine.closeOutbound();
        } catch (Throwable e) {
            LOGGER.info("close outbound error", e);
        }
    }
}
