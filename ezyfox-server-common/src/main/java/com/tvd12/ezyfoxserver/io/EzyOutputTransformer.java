package com.tvd12.ezyfoxserver.io;

public interface EzyOutputTransformer {

	@SuppressWarnings("rawtypes")
	Object transform(Object value, Class type);
	
}
