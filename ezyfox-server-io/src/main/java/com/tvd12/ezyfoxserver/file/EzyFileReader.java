package com.tvd12.ezyfoxserver.file;

import java.io.File;
import java.util.Collection;

public interface EzyFileReader {

	byte[] readBytes(File file);
	
	Collection<String> readLines(File file, String charset);
	
}
