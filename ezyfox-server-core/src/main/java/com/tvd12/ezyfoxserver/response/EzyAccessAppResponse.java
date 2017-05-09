package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

import lombok.Setter;

@Setter
public class EzyAccessAppResponse extends EzyBaseResponse implements EzyResponse {

	protected EzyData data;
	protected EzyAppSetting app;
	
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
	
	public static class Builder implements EzyResponse.Builder {
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
		    EzyAccessAppResponse answer = new EzyAccessAppResponse();
		    answer.setApp(app);
		    answer.setData(data);
		    return answer;
		}

	}

}
