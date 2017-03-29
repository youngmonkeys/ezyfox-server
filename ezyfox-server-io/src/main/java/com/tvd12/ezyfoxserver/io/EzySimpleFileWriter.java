package com.tvd12.ezyfoxserver.io;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class EzySimpleFileWriter implements EzyFileWriter {

	@Override
	public void write(byte[] data, File file) {
		try {
			FileUtils.writeByteArrayToFile(file, data);
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
