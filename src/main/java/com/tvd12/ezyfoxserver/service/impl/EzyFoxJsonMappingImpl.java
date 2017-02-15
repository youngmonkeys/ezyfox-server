package com.tvd12.ezyfoxserver.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tvd12.ezyfoxserver.service.EzyFoxJsonMapping;

public class EzyFoxJsonMappingImpl implements EzyFoxJsonMapping {

	private final ObjectMapper mapper;
	
	protected EzyFoxJsonMappingImpl(Builder builder) {
		this.mapper = new ObjectMapper();
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
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
	
	private Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		public EzyFoxJsonMappingImpl build() {
			return new EzyFoxJsonMappingImpl(this);
		}
		
	}

}
