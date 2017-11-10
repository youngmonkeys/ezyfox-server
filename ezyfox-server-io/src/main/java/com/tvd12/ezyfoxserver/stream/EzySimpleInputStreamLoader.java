package com.tvd12.ezyfoxserver.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.exception.EzyFileNotFoundException;
import com.tvd12.ezyfoxserver.file.EzyFileFetcher;
import com.tvd12.ezyfoxserver.file.EzySimpleFileFetcher;

public class EzySimpleInputStreamLoader implements EzyInputStreamLoader {

	protected EzyFileFetcher fileFetcher;
	
	protected EzySimpleInputStreamLoader(Builder builder) {
		this.fileFetcher = builder.newFileFecher();
	}
	
	@Override
	public InputStream load(String filePath) {
		try {
			File file = getFile(filePath);
			return file != null ? new FileInputStream(file) : null;
		} catch (Exception e) {
			throw processException(e);
		}
	}
	
	protected RuntimeException processException(Exception e) {
		if(e instanceof EzyFileNotFoundException)
			return (EzyFileNotFoundException)e;
		if(e instanceof FileNotFoundException) 
			return new EzyFileNotFoundException(e);
		throw new IllegalArgumentException(e);
	}
	
	protected File getFile(String filePath) throws FileNotFoundException {
		return fileFetcher.get(filePath);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder 
			implements EzyBuilder<EzyInputStreamLoader> {
		
		protected boolean throwException = true;
		
		public Builder throwException(boolean throwException) {
			this.throwException = throwException;
			return this;
		}
		
		@Override
		public EzyInputStreamLoader build() {
			return new EzySimpleInputStreamLoader(this);
		}
		
		protected EzyFileFetcher newFileFecher() {
			return EzySimpleFileFetcher.builder()
					.throwException(throwException)
					.build();
		}
	}
}
