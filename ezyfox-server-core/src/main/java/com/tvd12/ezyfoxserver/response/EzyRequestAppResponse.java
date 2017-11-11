package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzyRequestAppResponse extends EzyBaseResponse implements EzyResponse {

    protected int appId;
    protected EzyData data;
    
    protected EzyRequestAppResponse(Builder builder) {
        super(builder);
        this.data = builder.data;
        this.appId = builder.appId;
    }
	
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
	
	public static class Builder extends EzyBaseResponse.Builder<Builder> {
	    
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
		    return new EzyRequestAppResponse(this);
		}
		
	}

}
