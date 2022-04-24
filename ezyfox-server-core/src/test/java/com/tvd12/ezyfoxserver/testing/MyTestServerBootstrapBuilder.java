package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyAbstractServerBootstrapBuilder;

public class MyTestServerBootstrapBuilder extends EzyAbstractServerBootstrapBuilder {

    @Override
    protected EzyServerBootstrap newServerBootstrap() {
        return new MyTestServerBootstrap();
    }
}
