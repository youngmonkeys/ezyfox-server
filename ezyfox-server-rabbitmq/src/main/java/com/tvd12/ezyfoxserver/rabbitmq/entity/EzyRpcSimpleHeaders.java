package com.tvd12.ezyfoxserver.rabbitmq.entity;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzyRpcSimpleHeaders implements EzyRpcHeaders {

	protected Map<Object, Object> headers;
	
	protected EzyRpcSimpleHeaders(Builder builder) {
		this.headers = builder.headers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key) {
		return (T) headers.get(key);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRpcHeaders> {
		protected Map<Object, Object> headers = new HashMap<>();
		
		public Builder put(Object key, Object value) {
			headers.put(key, value);
			return this;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Builder putAll(Map map) {
			if(map != null)
				headers.putAll(map);
			return this;
		}
		
		@Override
		public EzyRpcHeaders build() {
			return new EzyRpcSimpleHeaders(this);
		}
	}
	
	@Override
	public String toString() {
		return "(#headers: " + headers + ")";
	}
	
}
