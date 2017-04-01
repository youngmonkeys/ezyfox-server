package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.entity.EzyProperties;

public interface EzyAppEntryLoader extends EzyProperties {

	EzyAppEntry load() throws Exception;
	
}
