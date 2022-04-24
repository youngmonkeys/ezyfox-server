package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;

public interface EzyServerBootstrapBuilder extends EzyBuilder<EzyServerBootstrap> {

    EzyServerBootstrapBuilder server(EzyServer server);
}
