package com.tvd12.ezyfoxserver.admintools.factory.impl;

import org.springframework.stereotype.Component;

import com.tvd12.ezyfoxserver.admintools.factory.EzyRestCallerFactory;
import com.tvd12.restful.client.RestCaller;

@Component("restCallerFactory")
public class EzyRestCallerFactoryImpl implements EzyRestCallerFactory {

	@Override
	public RestCaller newRestCaller() {
		return RestCaller.create()
				.template()
					.connectTimeOut(10 * 1000)
					.readTimeOut(10 * 1000)
		        .done();
	}
	
}
