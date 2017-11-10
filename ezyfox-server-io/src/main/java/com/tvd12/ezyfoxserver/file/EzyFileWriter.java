package com.tvd12.ezyfoxserver.file;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface EzyFileWriter {

	void write(File file, byte[] data);
	
	void write(File file, InputStream stream);
	
	void write(File file, CharSequence data, String charset);
	
	void write(File file, CharSequence data, Charset charset);
	
}
