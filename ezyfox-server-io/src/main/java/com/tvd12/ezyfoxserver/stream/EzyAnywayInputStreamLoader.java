package com.tvd12.ezyfoxserver.stream;

import java.io.InputStream;

public class EzyAnywayInputStreamLoader extends EzyClassPathInputStreamLoader {

	protected EzyInputStreamLoader firstLoader;
	
	protected EzyAnywayInputStreamLoader(Builder builder) {
		super(builder);
		this.firstLoader = builder.newFirstLoader();
	}
	
	@Override
	public InputStream load(String filePath) {
		InputStream stream = tryFirstLoad(filePath);
		return stream != null ? stream : super.load(filePath);
	}
	
	protected InputStream tryFirstLoad(String filePath) {
		return firstLoader.load(filePath);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyClassPathInputStreamLoader.Builder {
		
		@Override
		public EzyAnywayInputStreamLoader build() {
			return new EzyAnywayInputStreamLoader(this);
		}
		
		protected EzyInputStreamLoader newFirstLoader() {
			return EzySimpleInputStreamLoader.builder()
					.throwException(false)
					.build();
		}
		
	}

}
