package com.tvd12.ezyfoxserver.rabbitmq.entity;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"headers", "body"})
public class EzyRpcSimpleResponseEntity implements EzyRpcResponseEntity {
	
	protected Object body;
	protected EzyRpcHeaders headers; 
	
	protected EzyRpcSimpleResponseEntity(Builder builder) {
		this.body = builder.body;
		this.headers = builder.headers;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRpcSimpleResponseEntity> {
		protected Object body;
		protected EzyRpcHeaders headers; 
		
		public Builder body(Object body) {
			this.body = body;
			return this;
		}
		
		public Builder headers(EzyRpcHeaders headers) {
			this.headers = headers;
			return this;
		}
		
		@Override
		public EzyRpcSimpleResponseEntity build() {
			return new EzyRpcSimpleResponseEntity(this);
		}
	}
	
}
