package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.support.command.ArrayResponse;
import com.tvd12.ezyfoxserver.support.command.ObjectResponse;
import com.tvd12.ezyfoxserver.support.command.PluginArrayResponse;
import com.tvd12.ezyfoxserver.support.command.PluginObjectResponse;

import lombok.Setter;

@Setter
@EzySingleton("pluginResponseFactory")
public class PluginResponseFactory extends AbstractResponseFactory {

	@EzyAutoBind
	protected EzyPluginContext pluginContext;
	
	@Override
	public ArrayResponse newArrayResponse() {
		return new PluginArrayResponse(pluginContext, marshaller);
	}
	
	@Override
	public ObjectResponse newObjectResponse() {
		return new PluginObjectResponse(pluginContext, marshaller);
	}
	
}
