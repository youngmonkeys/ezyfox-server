package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.entity.EzyProperties;

public interface EzyPluginEntryLoader extends EzyProperties {

	EzyPluginEntry load() throws Exception;
	
}
