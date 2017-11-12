package com.tvd12.ezyfoxserver.monitor;

import lombok.Getter;

@Getter
public class EzyMonitor {
	
	protected EzyCpuMonitor cpuMonitor;
	protected EzyMemoryMonitor memoryMonitor;
	protected EzyThreadsMonitor threadsMonitor;

	public EzyMonitor() {
		this.cpuMonitor = new EzyCpuMonitor();
		this.memoryMonitor = new EzyMemoryMonitor();
		this.threadsMonitor = new EzyThreadsMonitor();
	}
	
}
