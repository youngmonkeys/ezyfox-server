package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfox.util.EzyProperties;

public interface EzyAppEntryLoader extends EzyProperties {

	EzyAppEntry load() throws Exception;
	
}
