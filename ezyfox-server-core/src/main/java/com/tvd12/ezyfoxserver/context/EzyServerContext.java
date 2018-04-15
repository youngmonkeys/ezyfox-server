package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyServer;

public interface EzyServerContext extends EzyComplexContext {

	EzyServer getServer();
	
	EzyZoneContext getZoneContext(int zoneId);
	
	EzyZoneContext getZoneContext(String zoneName);
	
}
