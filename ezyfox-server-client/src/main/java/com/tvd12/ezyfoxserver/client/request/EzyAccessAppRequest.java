package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzyAccessAppRequest extends EzyBaseRequest implements EzyRequest {

	private String appName;
	private EzyData data;
	
	protected EzyAccessAppRequest(Builder builder) {
		this.data = builder.data;
		this.appName = builder.appName;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyClientCommand.APP_ACCESS;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
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
