package com.tvd12.ezyfoxserver.webapi.controller;

import com.tvd12.ezyfox.monitor.EzyCpuMonitor;
import com.tvd12.ezyfox.monitor.EzyMonitor;
import com.tvd12.ezyfoxserver.databridge.statistics.EzyCpuPoint;

public class EzyCpuController {

	protected EzyMonitor monitor;
	
	public EzyCpuPoint getDetails() {
		EzyCpuPoint point = new EzyCpuPoint();
		EzyCpuMonitor cpuMonitor = monitor.getCpuMonitor();
		point.setSystemCpuLoad(cpuMonitor.getSystemCpuLoad());
		point.setProcessCpuLoad(cpuMonitor.getProcessCpuLoad());
		return point;
	}
	
}
