package com.tvd12.ezyfoxserver.service;

import java.io.File;

public interface EzyXmlReading {

	<T> T read(final File xmlFile, final Class<T> outputType);
	
	default <T> T read(final String filePath, final Class<T> outputType) {
		return read(new File(filePath), outputType);
	}
	
}
