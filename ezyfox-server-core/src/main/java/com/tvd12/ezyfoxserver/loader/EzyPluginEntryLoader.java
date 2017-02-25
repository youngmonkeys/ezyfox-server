package com.tvd12.ezyfoxserver.loader;

import com.tvd12.ezyfoxserver.entity.EzyProperties;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;

public interface EzyPluginEntryLoader extends EzyProperties {

	EzyPluginEntry load() throws Exception;
	
}
