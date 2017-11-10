package com.tvd12.ezyfoxserver.client.request;

import java.util.HashMap;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EzyFixedCmdRequest
		extends EzyBaseRequest
		implements EzyRequest {

	protected Object data;
	
	protected EzyFixedCmdRequest(Builder<?> builder) {
		this.data = builder.getData();
	}
	
	public abstract static class Builder<B extends Builder<B>> 
			implements EzyBuilder<EzyRequest> {
		protected Object data;
		
		public B data(Object data) {
			this.data = data;
			return getThis();
		}
		
		@SuppressWarnings("unchecked")
		protected B getThis() {
			return (B)this;
		}
		
		protected Object getData() {
			return data != null ? data : new HashMap<>();
		}
	}
	
}
