package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzyLoginRequest extends EzyBaseRequest implements EzyRequest {

	private String username;
	private String password;
	private EzyData data;
	
	protected EzyLoginRequest(Builder builder) {
		this.data = builder.data;
		this.username = builder.username;
		this.password = builder.password;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyClientCommand.LOGIN;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
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
