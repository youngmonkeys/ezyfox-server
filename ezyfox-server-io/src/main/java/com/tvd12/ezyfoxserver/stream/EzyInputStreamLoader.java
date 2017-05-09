package com.tvd12.ezyfoxserver.stream;

import java.io.InputStream;

public interface EzyInputStreamLoader {

	InputStream load(String filePath);
	
}
