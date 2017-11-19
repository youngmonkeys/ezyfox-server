package com.tvd12.ezyfoxserver.admintools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.tvd12.ezyfoxserver.admintools.factory.EzyRestCallerFactory;
import com.tvd12.restful.client.RestCaller;

public abstract class EzyWepApiService {

	@Value("${ezyfox.webapi.url}")
	protected String webApiUrl;
	
	@Autowired
	protected EzyRestCallerFactory restcallerFactory;
	
	protected RestCaller newRestCaller(String path) {
		return restcallerFactory.newRestCaller()
			.uri()
			.url(webApiUrl)
			.path(path)
		.done();
	}
	
	protected <T> T get(String path, Class<T> type) {
		RestCaller caller = newRestCaller(path);
		return caller.get(type);
	}
	
}
