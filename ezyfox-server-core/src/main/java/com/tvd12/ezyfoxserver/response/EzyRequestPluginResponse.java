package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzyRequestPluginResponse extends EzyBaseResponse implements EzyResponse {

    protected EzyData data;
    protected String pluginName;
    
    protected EzyRequestPluginResponse(Builder builder) {
        super(builder);
        this.data = builder.data;
        this.pluginName = builder.pluginName;
    }
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.APP_REQUEST;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(pluginName)
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyBaseResponse.Builder<Builder> {
	    protected EzyData data;
	    protected String pluginName;
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		public Builder pluginName(String pluginName) {
            this.pluginName = pluginName;
            return this;
        }
		
		@Override
		public EzyResponse build() {
		    return new EzyRequestPluginResponse(this);
		}
		
	}

}
