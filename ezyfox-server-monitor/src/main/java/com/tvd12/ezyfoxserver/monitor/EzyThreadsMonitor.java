package com.tvd12.ezyfoxserver.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfoxserver.monitor.data.EzyThreadDetail;
import com.tvd12.ezyfoxserver.monitor.data.EzyThreadDetails;
import com.tvd12.ezyfoxserver.monitor.data.EzyThreadsDetail;

public class EzyThreadsMonitor {
	
	public int getThreadCount() {
		ThreadMXBean tmxBean = getThreadMXBean();
		return tmxBean.getThreadCount();
	}
	
	public EzyThreadDetails getThreadDetails(long threadId) {
		ThreadMXBean tmxBean = getThreadMXBean();
		ThreadInfo info = tmxBean.getThreadInfo(threadId);
		if(info == null) return null;
		return new EzyThreadDetails(info);
	}

	public EzyThreadsDetail getThreadsDetails() {
		long totalThreadsCpuTime = 0L;
		ThreadMXBean tmxBean = getThreadMXBean();
		long[] threadIds = tmxBean.getAllThreadIds();
		List<EzyThreadDetail> threads = new ArrayList<>();
		for (long threadId : threadIds) {
			ThreadInfo threadInfo = tmxBean.getThreadInfo(threadId);
			if(threadInfo == null)
				continue;
			String threadName = threadInfo.getThreadName();
			long cpuTime = 0L;
			if (canGetThreadCpuTime()) {
				cpuTime = tmxBean.getThreadCpuTime(threadId);
				if(cpuTime > 0)
					totalThreadsCpuTime += cpuTime;
			}
			EzyThreadDetail detail = new EzyThreadDetail();
			detail.setId(threadId);
			detail.setName(threadName);
			detail.setCpuTime(cpuTime);
			threads.add(detail);
		}
		EzyThreadsDetail details = new EzyThreadsDetail();
		details.setThreads(threads);
		details.setThreadsSize(threads.size());
		details.setTotalThreadsCpuTime(totalThreadsCpuTime);
		return details;
	}
	
	protected boolean canGetThreadCpuTime() {
		return isThreadCpuTimeEnabled() && isThreadCpuTimeSupported();
	}
	
	protected boolean isThreadCpuTimeEnabled() {
		return getThreadMXBean().isThreadCpuTimeEnabled();
	}
	
	protected boolean isThreadCpuTimeSupported() {
		return getThreadMXBean().isThreadCpuTimeSupported();
	}
	
	protected ThreadMXBean getThreadMXBean() {
		return ManagementFactory.getThreadMXBean();
	}
}
