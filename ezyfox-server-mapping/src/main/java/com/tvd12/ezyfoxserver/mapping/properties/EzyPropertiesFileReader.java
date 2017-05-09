package com.tvd12.ezyfoxserver.mapping.properties;

public interface EzyPropertiesFileReader {

	<T> T read(String filePath, Class<T> valueType);
	
}
