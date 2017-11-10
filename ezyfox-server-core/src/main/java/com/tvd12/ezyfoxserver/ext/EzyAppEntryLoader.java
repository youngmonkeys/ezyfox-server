package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.util.EzyProperties;

public interface EzyAppEntryLoader extends EzyProperties {

	EzyAppEntry load() throws Exception;
	
}
