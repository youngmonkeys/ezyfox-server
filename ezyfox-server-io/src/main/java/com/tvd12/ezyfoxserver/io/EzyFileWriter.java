package com.tvd12.ezyfoxserver.io;

import java.io.File;

public interface EzyFileWriter {

	void write(byte[] data, File file);
	
}
