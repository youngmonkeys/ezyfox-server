package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyServer;

public interface EzyServerContext extends EzyContext {

	EzyServer getBoss();
	
}
