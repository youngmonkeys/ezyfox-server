package com.tvd12.ezyfoxserver.mapping.jaxb;

import java.io.File;

public interface EzyXmlReader {

	<T> T read(File xmlFile, Class<T> outputType);
	
	default <T> T read(String filePath, Class<T> outputType) {
		return read(new File(filePath), outputType);
	}
	
}
