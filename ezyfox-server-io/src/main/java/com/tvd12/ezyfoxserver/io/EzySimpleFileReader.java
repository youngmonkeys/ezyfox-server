package com.tvd12.ezyfoxserver.io;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class EzySimpleFileReader implements EzyFileReader {

	@Override
	public byte[] readBytes(File file) {
		try {
			return FileUtils.readFileToByteArray(file);
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	
	
}
