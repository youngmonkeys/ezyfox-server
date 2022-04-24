package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;

public class MyTestServerBootstrap extends EzyServerBootstrap {

    @Override
    protected void startOtherBootstraps(Runnable callback) throws Exception {
        callback.run();
    }
}
