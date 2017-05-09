package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyRequestPluginRequest extends EzyBaseRequest implements EzyRequest {

	protected EzyArray data;
	protected String pluginName;
	
	protected EzyRequestPluginRequest(Builder builder) {
		this.data = builder.data;
		this.pluginName = builder.pluginName;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyClientCommand.PLUGIN_REQUEST;
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
	
	public static class Builder implements EzyBuilder<EzyRequest> {
	
		protected EzyArray data;
		protected String pluginName;
		
		public Builder data(EzyArray data) {
			this.data = data;
			return this;
		}
		
		public Builder pluginName(String pluginName) {
			this.pluginName = pluginName;
			return this;
		}
		
		@Override
		public EzyRequest build() {
			return new EzyRequestPluginRequest(this);
		}
	}

}
