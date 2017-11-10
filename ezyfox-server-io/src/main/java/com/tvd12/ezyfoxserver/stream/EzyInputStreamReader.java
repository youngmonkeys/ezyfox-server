package com.tvd12.ezyfoxserver.stream;

import java.io.InputStream;
import java.util.Collection;

public interface EzyInputStreamReader {

	byte[] readBytes(InputStream stream);
	
	char[] readChars(InputStream stream, String charset);
	
	String readString(InputStream stream, String charset);
	
	Collection<String> readLines(InputStream stream, String charset);
	
}
