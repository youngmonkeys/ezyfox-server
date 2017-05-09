package com.tvd12.ezyfoxserver.file;

import java.io.File;
import java.net.URL;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzyClassPathFileFetcher implements EzyFileFetcher {

	protected ClassLoader classLoader;
	
	protected EzyClassPathFileFetcher(Builder builder) {
		this.classLoader = builder.classLoader;
	}
	
	@Override
	public File get(String filePath) {
		File file = firstGet(filePath);
		return file != null ? file : secondGet(filePath);
	}
	
	protected File firstGet(String filePath) {
		URL url = classLoader.getResource(filePath);
		return url != null ? new File(url.getFile()) : null;
	}
	
	protected File secondGet(String filePath) {
		return firstGet(File.separator + filePath);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyFileFetcher> {
		protected ClassLoader classLoader = getClass().getClassLoader();
		
		public Builder context(Class<?> context) {
			return classLoader(context.getClassLoader());
		}
		
		public Builder classLoader(ClassLoader classLoader) {
			this.classLoader = classLoader;
			return this;
		}
		
		@Override
		public EzyFileFetcher build() {
			return new EzyClassPathFileFetcher(this);
		}
	}

}
