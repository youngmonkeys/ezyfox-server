package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.util.EzyProperties;

public interface EzyContext extends EzyProperties {

	<T> T get(Class<T> clazz);
	
}
