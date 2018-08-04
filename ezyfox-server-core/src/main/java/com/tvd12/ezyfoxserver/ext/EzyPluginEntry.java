package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;

public interface EzyPluginEntry extends EzyEntry, EzyDestroyable {
	
	void config(EzyPluginContext ctx);
	
}
