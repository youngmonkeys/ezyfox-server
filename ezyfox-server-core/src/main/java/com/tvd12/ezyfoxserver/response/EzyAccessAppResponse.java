package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

public class EzyAccessAppResponse extends EzyBaseResponse implements EzyResponse {

	protected EzyData data;
	protected EzyAppSetting app;
	
	public EzyAccessAppResponse(Builder builder) {
	    super(builder);
	    this.app = builder.app;
	    this.data = builder.data;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.APP_ACCESS;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(app.getId())
				.append(app.getName())
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyBaseResponse.Builder<Builder> {
		protected EzyData data;
		protected EzyAppSetting app;
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		public Builder app(EzyAppSetting app) {
            this.app = app;
            return this;
        }
		
		@Override
		public EzyAccessAppResponse build() {
		    return new EzyAccessAppResponse(this);
		}

	}

}
