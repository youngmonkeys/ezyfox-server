package com.tvd12.ezyfoxserver.webapi;

import org.springframework.core.env.PropertySource;

import com.tvd12.ezyfoxserver.context.EzyContexts;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;

public class EzyWebApiPropertySource extends PropertySource<Object> {

	protected EzyServerContext context;
	protected EzyHttpSetting httpSetting;
	
	public EzyWebApiPropertySource(EzyServerContext context) {
		super("ezyfox-server-webapi");
		this.context = context;
		this.httpSetting = EzyContexts.getHttpSetting(context);
	}
	
	@Override
	public Object getProperty(String name) {
		if(name.equals("server.tomcat.max-threads"))
			return httpSetting.getMaxThreads();
		if(name.equals("jetty.threadPool.maxThreads"))
			return httpSetting.getMaxThreads();
		return null;
	}
	
}
