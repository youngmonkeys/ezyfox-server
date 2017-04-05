package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyLoginResponse extends EzyBaseResponse implements EzyResponse {

	private long userId;
	private Object data;
	private String username;
	
	protected EzyLoginResponse(Builder builder) {
		this.data = builder.data;
		this.userId = builder.userId;
		this.username = builder.username;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.LOGIN;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(userId)
				.append(username)
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private long userId;
		private Object data;
		private String username;
		
		public Builder userId(long userId) {
			this.userId = userId;
			return this;
		}
		
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public EzyLoginResponse build() {
			return new EzyLoginResponse(this);
		}
	}

}
