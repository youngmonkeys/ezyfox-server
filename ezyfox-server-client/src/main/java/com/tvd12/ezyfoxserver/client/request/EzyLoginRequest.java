package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

public class EzyLoginRequest implements EzyRequest {

	private String username;
	private String password;
	private EzyData data;
	
	protected EzyLoginRequest(Builder builder) {
		this.data = builder.data;
		this.username = builder.username;
		this.password = builder.password;
	}
	
	@Override
	public int getAppId() {
		return -1;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.LOGIN;
	}
	
	@Override
	public Object getData() {
		return EzyFactory.create(EzyArrayBuilder.class)
				.append(username)
				.append(password)
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String username;
		private String password;
		private EzyData data;
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public EzyLoginRequest build() {
			return new EzyLoginRequest(this);
		}
	}

}
