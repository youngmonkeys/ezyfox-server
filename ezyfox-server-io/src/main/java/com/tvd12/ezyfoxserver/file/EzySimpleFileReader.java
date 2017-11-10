package com.tvd12.ezyfoxserver.file;

import static com.tvd12.ezyfoxserver.util.EzyReturner.returnWithIllegalArgumentException;
import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzySimpleFileReader implements EzyFileReader {
	
	protected EzySimpleFileReader(Builder builder) {
	}

	@Override
	public byte[] readBytes(File file) {
		return returnWithIllegalArgumentException(() -> FileUtils.readFileToByteArray(file));
	}
	
	@Override
	public Collection<String> readLines(File file, String charset) {
		return returnWithIllegalArgumentException(() -> FileUtils.readLines(file, charset));
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyFileReader> {

		@Override
		public EzyFileReader build() {
			return new EzySimpleFileReader(this);
		}
	}
	
}
