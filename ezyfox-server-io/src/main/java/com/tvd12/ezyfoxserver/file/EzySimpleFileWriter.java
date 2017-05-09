package com.tvd12.ezyfoxserver.file;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithIllegalArgumentException;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzySimpleFileWriter implements EzyFileWriter {

	protected EzySimpleFileWriter(Builder builder) {
	}
	
	@Override
	public void write(File file, byte[] data) {
		processWithIllegalArgumentException(() -> FileUtils.writeByteArrayToFile(file, data));
	}
	
	@Override
	public void write(File file, InputStream stream) {
		processWithIllegalArgumentException(() -> write(file, IOUtils.toByteArray(stream)));
	}
	
	@Override
	public void write(File file, CharSequence data, Charset charset) {
		processWithIllegalArgumentException(() -> FileUtils.write(file, data, charset));
	}
	
	@Override
	public void write(File file, CharSequence data, String charset) {
		write(file, data, Charset.forName(charset));
	}
	
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyFileWriter> {

		@Override
		public EzyFileWriter build() {
			return new EzySimpleFileWriter(this);
		}
	}
	
}
