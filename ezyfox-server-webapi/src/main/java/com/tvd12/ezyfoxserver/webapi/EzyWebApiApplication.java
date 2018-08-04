package com.tvd12.ezyfoxserver.webapi;

import com.tvd12.ezyfox.monitor.EzyMonitor;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

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
