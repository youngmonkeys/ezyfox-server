package com.tvd12.ezyfoxserver.monitor;

public class EzyMemoryMonitor {

	public long getFreeMemory() {
		return getRuntime().freeMemory();
	}
	
	public long getMaxMemory() {
		return getRuntime().maxMemory();
	}
	
	public long getTotalMemory() {
		return getRuntime().totalMemory();
	}
	
	private Runtime getRuntime() {
		return Runtime.getRuntime();
	}
	
}
