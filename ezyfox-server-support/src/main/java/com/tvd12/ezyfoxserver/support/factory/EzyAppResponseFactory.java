package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.support.command.EzyAppArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyAppObjectResponse;
import com.tvd12.ezyfoxserver.support.command.EzyArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;

import lombok.Setter;

@Setter
public class EzyAppResponseFactory extends EzyAbstractResponseFactory {

	protected EzyAppContext appContext;
	
	@Override
	public EzyArrayResponse newArrayResponse() {
		return new EzyAppArrayResponse(appContext, marshaller);
	}
	
	@Override
	public EzyObjectResponse newObjectResponse() {
		return new EzyAppObjectResponse(appContext, marshaller);
	}
	
}
