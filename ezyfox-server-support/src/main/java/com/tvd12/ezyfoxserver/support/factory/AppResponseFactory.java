package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.support.command.AppArrayResponse;
import com.tvd12.ezyfoxserver.support.command.AppObjectResponse;
import com.tvd12.ezyfoxserver.support.command.ArrayResponse;
import com.tvd12.ezyfoxserver.support.command.ObjectResponse;

import lombok.Setter;

@Setter
@EzySingleton("appResponseFactory")
public class AppResponseFactory extends AbstractResponseFactory {

	@EzyAutoBind
	protected EzyAppContext appContext;
	
	@Override
	public ArrayResponse newArrayResponse() {
		return new AppArrayResponse(appContext, marshaller);
	}
	
	@Override
	public ObjectResponse newObjectResponse() {
		return new AppObjectResponse(appContext, marshaller);
	}
	
}
