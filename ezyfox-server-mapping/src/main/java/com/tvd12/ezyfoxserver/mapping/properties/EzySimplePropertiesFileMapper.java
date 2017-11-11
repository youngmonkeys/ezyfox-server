package com.tvd12.ezyfoxserver.mapping.properties;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.reader.FileReader;

import lombok.Setter;

@Setter
public class EzySimplePropertiesFileMapper 
		extends EzyLoggable 
		implements EzyPropertiesFileMapper {

	@SuppressWarnings("rawtypes")
	protected Class context;
	
	protected EzySimplePropertiesFileMapper(Builder builder) {
		this.context = builder.context;
	}
	
	@Override
	public <T> T read(String filePath, Class<T> valueType) {
		return new PropertiesMapper()
				.context(context)
				.clazz(valueType)
				.file(filePath)
				.reader(newFileReader())
				.map();
	}
	
	protected FileReader newFileReader() {
		return new BaseFileReader();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyPropertiesFileMapper> {
		
		@SuppressWarnings("rawtypes")
		protected Class context = getClass();
		
		@SuppressWarnings("rawtypes")
		public Builder context(Class context) {
			this.context = context;
			return this;
		}
		
		@Override
		public EzyPropertiesFileMapper build() {
			return new EzySimplePropertiesFileMapper(this);
		}
	}
	
}
