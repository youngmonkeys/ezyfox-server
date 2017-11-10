package com.tvd12.ezyfoxserver.client.cmd.impl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.ezyfoxserver.client.cmd.EzyPingSchedule;
import com.tvd12.ezyfoxserver.client.cmd.EzySendRequest;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.request.EzyPingRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyPingScheduleImpl 
		extends EzyLoggable implements EzyPingSchedule {

	protected long delay = 3000;
	protected long period = 3000;
	protected EzyClientContext context;
	protected ScheduledExecutorService service;
	
	public EzyPingScheduleImpl(EzyClientContext context) {
		this.context = context;
		this.service = EzyExecutors.newSingleThreadScheduledExecutor("client-ping-service");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> service.shutdown()));
	}
	
	@Override
	public void start() {
		getLogger().debug("start ping schedule");
		service.scheduleAtFixedRate(this::execute, delay, period, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void stop() {
		service.shutdownNow();
		getLogger().debug("stop ping schedule");
	}
	
	protected void execute() {
		try {
			context
				.get(EzySendRequest.class)
				.sender(context.getMe())
				.request(newPingRequest())
				.execute();
		}
		catch(Exception e) {
			getLogger().error("send ping to server error", e);
		}
	}
	
	protected EzyRequest newPingRequest() {
		return EzyPingRequest.builder().build();
	}
	
}
