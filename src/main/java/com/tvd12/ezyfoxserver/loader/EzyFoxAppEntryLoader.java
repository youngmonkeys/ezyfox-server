package com.tvd12.ezyfoxserver.loader;

import com.tvd12.ezyfoxserver.entity.EzyFoxProperties;
import com.tvd12.ezyfoxserver.ext.EzyFoxAppEntry;

public interface EzyFoxAppEntryLoader extends EzyFoxProperties {

	EzyFoxAppEntry load() throws Exception;
	
}
