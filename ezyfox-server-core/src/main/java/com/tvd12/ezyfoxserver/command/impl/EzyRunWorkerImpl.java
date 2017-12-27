package com.tvd12.ezyfoxserver.command.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.concurrent.EzyWorker;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyRunWorkerImpl extends EzyAbstractCommand implements EzyRunWorker {

	private final ExecutorService executor;
	
	@Override
	public void run(EzyWorker worker) {
		try {
			executor.submit(worker).get(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			getLogger().error("error when run worker " + worker, e);
		}
	}

}
