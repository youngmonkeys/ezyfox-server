package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

public interface EzyServerContextBuilder<B extends EzyServerContextBuilder<B>> 
        extends EzyBuilder<EzyServerContext> {

    B server(EzyServer server);

}
