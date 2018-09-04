package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.support.command.EzyArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;
import com.tvd12.ezyfoxserver.support.command.EzyPluginArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyPluginObjectResponse;

import lombok.Setter;

@Setter
@EzySingleton("pluginResponseFactory")
public class EzyPluginResponseFactory extends EzyAbstractResponseFactory {

	@EzyAutoBind
	protected EzyPluginContext pluginContext;
	
	@Override
	public EzyArrayResponse newArrayResponse() {
		return new EzyPluginArrayResponse(pluginContext, marshaller);
	}
	
	@Override
	public EzyObjectResponse newObjectResponse() {
		return new EzyPluginObjectResponse(pluginContext, marshaller);
	}
	
}
