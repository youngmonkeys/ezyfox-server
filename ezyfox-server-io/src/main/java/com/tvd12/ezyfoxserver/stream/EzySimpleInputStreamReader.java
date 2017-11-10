package com.tvd12.ezyfoxserver.stream;

import static com.tvd12.ezyfoxserver.util.EzyReturner.returnWithIllegalArgumentException;

import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.IOUtils;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzySimpleInputStreamReader implements EzyInputStreamReader {

	protected EzySimpleInputStreamReader(Builder builder) {
	}
	
	@Override
	public byte[] readBytes(InputStream stream) {
		return returnWithIllegalArgumentException(() -> IOUtils.toByteArray(stream));
	}
	
	@Override
	public String readString(InputStream stream, String charset) {
		return returnWithIllegalArgumentException(() -> new String(readBytes(stream), charset));
	}
	
	@Override
	public char[] readChars(InputStream stream, String charset) {
		return readString(stream, charset).toCharArray();
	}
	
	@Override
	public Collection<String> readLines(InputStream stream, String charset) {
		return returnWithIllegalArgumentException(() -> IOUtils.readLines(stream, charset));
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyInputStreamReader> {

		@Override
		public EzyInputStreamReader build() {
			return new EzySimpleInputStreamReader(this);
		}
	}
	
}
