package com.tvd12.ezyfoxserver.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.command.impl.EzyRunWorkerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendMessageImpl;
import com.tvd12.ezyfoxserver.entity.EzyEntity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class EzySimpleClientContext extends EzyEntity implements EzyClientContext {

	@Setter
	@Getter
	protected EzyClient client;
	@Setter
	@Getter
	protected ExecutorService workerExecutor;
	@Setter
	protected ChannelHandlerContext channelContext;
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> commandSuppliers;
	
	{
		commandSuppliers = defaultCommandSuppliers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz).get();
		if(containsKey(clazz))
			return getProperty(clazz);
		throw new IllegalArgumentException("has no instance of " + clazz);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new HashMap<>();
		answer.put(EzySendMessage.class, () -> new EzySendMessageImpl());
		answer.put(EzyRunWorker.class, () -> new EzyRunWorkerImpl(getWorkerExecutor()));
		return answer;
	}

}
