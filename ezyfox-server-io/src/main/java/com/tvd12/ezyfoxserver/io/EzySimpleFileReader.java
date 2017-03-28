package com.tvd12.ezyfoxserver.io;

import java.io.File;
import java.io.FileInputStream;

public class EzySimpleFileReader implements EzyFileReader {

	@SuppressWarnings("resource")
	@Override
	public byte[] readBytes(File file) {
		try {
			FileInputStream is = new FileInputStream(file);
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			return bytes;
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	
	
}
