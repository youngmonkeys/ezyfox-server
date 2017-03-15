package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

public class EzyHandShakeResponse implements EzyResponse {

	private String token;
	
	protected EzyHandShakeResponse(Builder builder) {
		this.token = builder.token;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.HAND_SHAKE;
	}
	
	@Override
	public Object getData() {
		return EzyFactory.create(EzyArrayBuilder.class)
				.append(token).build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String token;
		
		public Builder token(String token) {
			this.token = token;
			return this;
		}
		
		public EzyHandShakeResponse build() {
			return new EzyHandShakeResponse(this);
		}
	}

}
