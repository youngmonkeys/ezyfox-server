package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

public class EzyAccessAppResponse implements EzyResponse {

	private int appId;
	private EzyData data;
	
	protected EzyAccessAppResponse(Builder builder) {
		this.data = builder.data;
		this.appId = builder.appId;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.ACCESS_APP;
	}
	
	@Override
	public Object getData() {
		return EzyFactory.create(EzyArrayBuilder.class)
				.append(appId)
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private int appId;
		private EzyData data;
		
		public Builder appId(int appId) {
			this.appId = appId;
			return this;
		}
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		public EzyAccessAppResponse build() {
			return new EzyAccessAppResponse(this);
		}
	}

}
