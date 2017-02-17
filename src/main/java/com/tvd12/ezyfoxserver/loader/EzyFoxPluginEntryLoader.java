package com.tvd12.ezyfoxserver.loader;

import com.tvd12.ezyfoxserver.entity.EzyFoxProperties;
import com.tvd12.ezyfoxserver.ext.EzyFoxPluginEntry;

public interface EzyFoxPluginEntryLoader extends EzyFoxProperties {

	EzyFoxPluginEntry load() throws Exception;
	
}
