package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

import lombok.Setter;

@Setter
public class EzyRequestAppResponse extends EzyBaseResponse implements EzyResponse {

    protected int appId;
    protected EzyData data;
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.APP_REQUEST;
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
	
	public static class Builder implements EzyResponse.Builder {
	    protected int appId;
	    protected EzyData data;
		
		public Builder appId(int appId) {
			this.appId = appId;
			return this;
		}
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		@Override
		public EzyResponse build() {
		    EzyRequestAppResponse answer = new EzyRequestAppResponse();
		    answer.setData(data);
		    answer.setAppId(appId);
		    return answer;
		}
		
	}

}
