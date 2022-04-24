package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzyHttpServerBootstrap extends EzyServerBootstrap {

    private EzyHttpBootstrap httpBootstrap;

    protected void startHttpBootstrap() throws Exception {
        EzyHttpSetting setting = getHttpSetting();
        if (!setting.isActive()) {
            return;
        }
        logger.debug("starting http server bootstrap ....");
        httpBootstrap = newHttpBottstrap();
        httpBootstrap.start();
        logger.debug("http server bootstrap has started");
    }

    private EzyHttpBootstrap newHttpBottstrap() {
        EzyEmptyHttpBootstrap bootstrap = new EzyEmptyHttpBootstrap();
        bootstrap.setServerContext(context);
        return bootstrap;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (httpBootstrap != null) {
            processWithLogException(() -> httpBootstrap.destroy());
        }
        this.httpBootstrap = null;
    }
}
