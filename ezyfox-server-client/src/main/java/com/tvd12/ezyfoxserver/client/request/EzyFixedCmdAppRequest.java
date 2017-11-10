package com.tvd12.ezyfoxserver.client.request;

import lombok.Getter;

@Getter
public abstract class EzyFixedCmdAppRequest 
		extends EzyFixedCmdRequest 
		implements EzyAppRequest {

	protected int appId;
	protected Object data;
	
	protected EzyFixedCmdAppRequest(Builder<?> builder) {
		super(builder);
		this.data = builder.data;
		this.appId = builder.appId;
	}
	
	public abstract static class Builder<B extends Builder<B>> 
			extends EzyFixedCmdRequest.Builder<B> {
		
		protected int appId;
		protected Object data;
		
		public B appId(int appId) {
			this.appId = appId;
			return getThis();
		}
		
		public B data(Object data) {
			this.data = data;
			return getThis();
		}
	}
	
}
