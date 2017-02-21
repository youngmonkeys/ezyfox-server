package com.tvd12.ezyfoxserver.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;

public class EzyJsonMappingImpl implements EzyJsonMapping {

	private final ObjectMapper mapper;
	
	protected EzyJsonMappingImpl(Builder builder) {
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
		
		public EzyJsonMappingImpl build() {
			return new EzyJsonMappingImpl(this);
		}
		
	}

}
