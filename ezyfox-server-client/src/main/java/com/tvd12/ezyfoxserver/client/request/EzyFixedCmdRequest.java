package com.tvd12.ezyfoxserver.client.request;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EzyFixedCmdRequest
		extends EzyBaseRequest
		implements EzyRequest {

	protected Object data;
	
	protected EzyFixedCmdRequest(Builder<?> builder) {
		this.data = builder.data;
	}
	
	public abstract static class Builder<B extends Builder<B>> {
		protected Object data;
		
		public B data(Object data) {
			this.data = data;
			return getThis();
		}
		
		public EzyFixedCmdRequest build() {
			this.prepare();
			return newProduct();
		}
		
		@SuppressWarnings("unchecked")
		protected B getThis() {
			return (B)this;
		}
		
		protected Object defaultData() {
			return new HashMap<>();
		}
		
		protected void prepare() {
			if(data == null)
				data = defaultData();
		}
		
		protected abstract EzyFixedCmdRequest newProduct();
	}
	
}
