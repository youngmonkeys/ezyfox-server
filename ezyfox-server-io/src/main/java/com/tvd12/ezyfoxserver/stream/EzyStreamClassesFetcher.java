package com.tvd12.ezyfoxserver.stream;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.io.EzyClassesFetcher;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;

public class EzyStreamClassesFetcher implements EzyClassesFetcher {
	
	protected ClassLoader classLoader;
	
	protected EzyStreamClassesFetcher(Builder builder) {
		this.classLoader = builder.classLoader;
	}
	
	/* (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.file.EzyClassesFetcher#asSet()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Set<Class> asSet(String filePath) {
		return new HashSet<>(asList(filePath));
	}
	
	/* (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.file.EzyClassesFetcher#asList()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<Class> asList(String filePath) {
		Collection<String> classNames = getClassNames(filePath);
		return EzyLists.newArrayList(classNames, this::getClass);
	}
	
	@SuppressWarnings("rawtypes")
	protected Class getClass(String className) {
		return EzyClasses.getClass(className, classLoader);
	}
	
	protected Collection<String> getClassNames(String filePath) {
		InputStream stream = loadInputStream(filePath);
		EzyInputStreamReader reader = newInputStreamReader();
		return reader.readLines(stream, "UTF-8");
	}
	
	protected EzyInputStreamReader newInputStreamReader() {
		return new EzySimpleInputStreamReader.Builder()
				.build();
	}
	
	protected InputStream loadInputStream(String filePath) {
		EzyInputStreamLoader loader = newInputStreamLoader();
		return loader.load(filePath);
	}
	
	protected EzyInputStreamLoader newInputStreamLoader() {
		return new EzyAnywayInputStreamLoader.Builder()
				.classLoader(classLoader)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyClassesFetcher> {
		
		protected ClassLoader classLoader;
		
		public Builder context(Class<?> context) {
			return classLoader(context.getClassLoader());
		}
		
		public Builder classLoader(ClassLoader classLoader) {
			this.classLoader = classLoader;
			return this;
		}
		
		@Override
		public EzyClassesFetcher build() {
			return new EzyStreamClassesFetcher(this);
		}
	}
	
}
