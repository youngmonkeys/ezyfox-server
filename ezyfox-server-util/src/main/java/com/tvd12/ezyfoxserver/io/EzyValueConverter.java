package com.tvd12.ezyfoxserver.io;

public interface EzyValueConverter {
	
	<T> T convert(Object value, Class<T> outType);
	
}
