package com.tvd12.ezyfoxserver.webapi.controller;

import com.tvd12.ezyfox.monitor.EzyMonitor;
import com.tvd12.ezyfox.monitor.EzyThreadsMonitor;
import com.tvd12.ezyfox.monitor.data.EzyThreadDetails;
import com.tvd12.ezyfox.monitor.data.EzyThreadsDetail;
import com.tvd12.ezyfoxserver.webapi.exception.EzyThreadNotFoundException;

public class EzyThreadsController extends EzyAbstractController {

	protected EzyMonitor monitor;
	
	public int getThreadCount() {
		return getThreadsMonitor().getThreadCount();
	}

	public EzyThreadsDetail getThreads() {
		return getThreadsMonitor().getThreadsDetails();
	}
	
	public EzyThreadDetails getThread(long id) {
		if(id <= 0)
			throw EzyThreadNotFoundException.invalid(id);
		EzyThreadDetails details = getThreadsMonitor().getThreadDetails(id);
		if(details == null)
			throw EzyThreadNotFoundException.notFound(id);
		return details;
	}
	
	protected EzyThreadsMonitor getThreadsMonitor() {
		return monitor.getThreadsMonitor();
	}
	
}
