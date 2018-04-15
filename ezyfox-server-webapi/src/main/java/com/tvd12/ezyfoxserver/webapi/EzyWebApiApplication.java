package com.tvd12.ezyfoxserver.webapi;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.monitor.EzyMonitor;

import lombok.Setter;

@Setter
public class EzyWebApiApplication {

	protected EzyServerContext serverContext;
	
	public EzyWebApiApplication(Object... sources) {
	}
	
	protected EzyMonitor newMonitor() {
		return new EzyMonitor();
	}
	
}
