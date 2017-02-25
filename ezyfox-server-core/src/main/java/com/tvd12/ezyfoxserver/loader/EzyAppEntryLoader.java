package com.tvd12.ezyfoxserver.loader;

import com.tvd12.ezyfoxserver.entity.EzyProperties;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;

public interface EzyAppEntryLoader extends EzyProperties {

	EzyAppEntry load() throws Exception;
	
}
