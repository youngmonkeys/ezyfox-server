package com.tvd12.ezyfoxserver.monitor.data;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EzyThreadDetails extends EzyThreadDetail {

	protected long blockedTime;
	protected long blockedCount;
	protected long waitedTime;
	protected long waitedCount;
	protected String lockName;
	protected long lockOwnerId;
	protected String lockOwnerName;
	protected boolean inNative;
	protected boolean suspended;
	protected Thread.State state;
	protected String stackTrace;
	protected String overview;

	public EzyThreadDetails(ThreadInfo info) {
		ThreadMXBean tmxBean = getThreadMXBean();
		this.id = info.getThreadId();
		this.name = info.getThreadName();
		this.blockedTime = info.getBlockedTime();
		this.blockedCount = info.getBlockedCount();
		this.waitedTime = info.getWaitedTime();
		this.waitedCount = info.getWaitedCount();
		this.lockName = info.getLockName();
		this.lockOwnerId = info.getLockOwnerId();
		this.lockOwnerName = info.getLockOwnerName();
		this.inNative = info.isInNative();
		this.suspended = info.isSuspended();
		this.state = info.getThreadState();
		this.cpuTime = tmxBean.getThreadCpuTime(id);
		this.overview = info.toString();
	}
	
	protected ThreadMXBean getThreadMXBean() {
		return ManagementFactory.getThreadMXBean();
	}
	
	@Override
	public String toString() {
		return overview;
	}
}
