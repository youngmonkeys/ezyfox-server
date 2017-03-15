package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

public class EzyLoginResponse implements EzyResponse {

	private long userId;
	private EzyData data;
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
		return EzyFactory.create(EzyArrayBuilder.class)
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
		private EzyData data;
		private String username;
		
		public Builder userId(long userId) {
			this.userId = userId;
			return this;
		}
		
		public Builder data(EzyData data) {
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
