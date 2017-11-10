package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractRequest extends EzyFixedCmdRequest {

	protected EzyConstant command;
	
	protected EzyAbstractRequest(Builder<?> builder) {
		super(builder);
		this.command = builder.command;
	}
	
	public abstract static class Builder<B extends Builder<B>> 
			extends EzyFixedCmdRequest.Builder<B> {
		
		protected EzyConstant command;
		
		public B command(EzyConstant cmd) {
			this.command = cmd;
			return getThis();
		}
	}
	
}
