package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;

public interface EzyPluginEntry extends EzyEntry, EzyDestroyable {
	
	void config(EzyPluginContext ctx);
	
}
