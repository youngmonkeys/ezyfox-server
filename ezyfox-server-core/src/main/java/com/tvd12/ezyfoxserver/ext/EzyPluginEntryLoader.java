package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfox.util.EzyProperties;

public interface EzyPluginEntryLoader extends EzyProperties {

	EzyPluginEntry load() throws Exception;
	
}
