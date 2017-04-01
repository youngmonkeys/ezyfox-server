package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzyAccessAppResponse extends EzyBaseResponse implements EzyResponse {

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
		return newArrayBuilder()
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
