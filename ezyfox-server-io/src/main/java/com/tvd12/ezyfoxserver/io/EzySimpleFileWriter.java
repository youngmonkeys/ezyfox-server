package com.tvd12.ezyfoxserver.io;

import java.io.File;
import java.io.FileOutputStream;

public class EzySimpleFileWriter implements EzyFileWriter {

	@SuppressWarnings("resource")
	@Override
	public void write(byte[] data, File file) {
		try {
			new FileOutputStream(file).write(data);
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
