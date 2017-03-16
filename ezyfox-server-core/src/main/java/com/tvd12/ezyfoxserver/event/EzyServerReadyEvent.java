package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.EzyServer;

public interface EzyServerReadyEvent extends EzyEvent {

	EzyServer getServer();
	
}
