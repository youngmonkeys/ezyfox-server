package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.util.EzyProperties;

public interface EzyPluginEntryLoader extends EzyProperties {

	EzyPluginEntry load() throws Exception;
	
}
