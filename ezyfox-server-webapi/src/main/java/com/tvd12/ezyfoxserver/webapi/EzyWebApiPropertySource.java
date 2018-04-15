package com.tvd12.ezyfoxserver.webapi;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;

public class EzyWebApiPropertySource {

	protected EzyServerContext context;
	protected EzyHttpSetting httpSetting;
	
	public EzyWebApiPropertySource(EzyServerContext context) {
		this.context = context;
		this.httpSetting = EzyServerContexts.getHttpSetting(context);
	}
	
	public Object getProperty(String name) {
		if(name.equals("server.tomcat.max-threads"))
			return httpSetting.getMaxThreads();
		if(name.equals("jetty.threadPool.maxThreads"))
			return httpSetting.getMaxThreads();
		return null;
	}
	
}
