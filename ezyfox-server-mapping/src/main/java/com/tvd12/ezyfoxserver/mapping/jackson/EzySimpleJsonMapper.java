package com.tvd12.ezyfoxserver.mapping.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleJsonMapper 
        extends EzyLoggable implements EzyJsonMapper {

	private final ObjectMapper mapper;
	
	protected EzySimpleJsonMapper(Builder builder) {
		this.mapper = EzyObjectMapperBuilder.objectMapperBuilder().build();
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}
	
	@Override
	public String writeAsString(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			getLogger().error("can not read object " + object, e);
			return null;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		public EzySimpleJsonMapper build() {
			return new EzySimpleJsonMapper(this);
		}
		
	}

}
