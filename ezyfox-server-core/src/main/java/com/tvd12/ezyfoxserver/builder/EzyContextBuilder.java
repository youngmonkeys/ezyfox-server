package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyContext;

public interface EzyContextBuilder<B extends EzyContextBuilder<B>> extends EzyBuilder<EzyContext> {
	
	B boss(EzyServer boss);
	
}
