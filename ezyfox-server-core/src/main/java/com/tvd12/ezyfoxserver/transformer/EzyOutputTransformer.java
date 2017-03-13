package com.tvd12.ezyfoxserver.transformer;

public interface EzyOutputTransformer {

	@SuppressWarnings("rawtypes")
	Object transform(Object value, Class type);
	
}
