package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyZone;

public interface EzyZoneContext extends EzyComplexContext {

	EzyZone getZone();
	
	EzyServerContext getParent();
	
}
