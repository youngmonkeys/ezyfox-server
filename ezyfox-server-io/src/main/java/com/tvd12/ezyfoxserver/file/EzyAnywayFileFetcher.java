package com.tvd12.ezyfoxserver.file;

import java.io.File;

public class EzyAnywayFileFetcher extends EzyClassPathFileFetcher {

	protected EzyFileFetcher firstFetcher;
	
	protected EzyAnywayFileFetcher(Builder builder) {
		super(builder);
		this.firstFetcher = builder.newFirstFetcher();
	}
	
	@Override
	public File get(String filePath) {
		File file = tryFirstFetch(filePath);
		return file != null ? file : super.get(filePath);
	}
	
	protected File tryFirstFetch(String filePath) {
		return firstFetcher.get(filePath);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyClassPathFileFetcher.Builder {
		
		@Override
		public EzyClassPathFileFetcher build() {
			return new EzyAnywayFileFetcher(this);
		}
		
		protected EzyFileFetcher newFirstFetcher() {
			return EzySimpleFileFetcher.builder()
					.throwException(false)
					.build();
		}
		
	}

}
