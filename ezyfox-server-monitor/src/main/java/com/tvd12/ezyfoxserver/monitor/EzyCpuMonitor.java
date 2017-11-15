package com.tvd12.ezyfoxserver.monitor;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class EzyCpuMonitor {

	protected long lastProcessCpuTime = 0L;
	protected long lastSystemTime = System.nanoTime();
	protected OperatingSystemMXBean osMxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	public EzyCpuMonitor() {
		this.lastProcessCpuTime = osMxBean.getProcessCpuTime();
	}

	public synchronized double getProcessCpuLoad() {
		long systemTime = System.nanoTime();
		long processCpuTime = osMxBean.getProcessCpuTime();
		long offsetSystemTime = systemTime - lastSystemTime;
		long offsetProcessCpuTime = processCpuTime - lastProcessCpuTime;
		double cpuLoad = (1.0D * offsetProcessCpuTime) / offsetSystemTime;

		this.lastSystemTime = systemTime;
		this.lastProcessCpuTime = processCpuTime;
		
		int nrocessors = osMxBean.getAvailableProcessors();
		return cpuLoad / nrocessors;
	}

	public synchronized double getSystemCpuLoad() {
		return this.osMxBean.getSystemCpuLoad();
	}

}
