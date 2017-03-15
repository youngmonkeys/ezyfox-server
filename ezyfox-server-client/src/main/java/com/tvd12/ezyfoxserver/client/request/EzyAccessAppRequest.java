package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

public class EzyAccessAppRequest implements EzyRequest {

	private String appName;
	private EzyData data;
	
	protected EzyAccessAppRequest(Builder builder) {
		this.data = builder.data;
		this.appName = builder.appName;
	}
	
	@Override
	public int getAppId() {
		return -1;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.ACCESS_APP;
	}
	
	@Override
	public Object getData() {
		return EzyFactory.create(EzyArrayBuilder.class)
				.append(appName)
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String appName;
		private EzyData data;
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		public Builder appName(String appName) {
			this.appName = appName;
			return this;
		}
		
		public EzyAccessAppRequest build() {
			return new EzyAccessAppRequest(this);
		}
	}

}
