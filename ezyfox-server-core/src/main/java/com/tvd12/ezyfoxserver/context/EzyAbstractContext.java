package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.command.EzyAddExceptionHandler;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.command.impl.EzyAddExceptionHandlerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyRunWorkerImpl;
import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;

import lombok.Getter;
import lombok.Setter;

public abstract class EzyAbstractContext extends EzyEntity {

	@Setter
	@Getter
	protected ExecutorService workerExecutor;
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> commandSuppliers = defaultCommandSuppliers();
	
	protected abstract EzyExceptionHandlersFetcher getExceptionHandlersFetcher();
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		addCommandSuppliers(answer);
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		suppliers.put(EzyRunWorker.class, () -> new EzyRunWorkerImpl(getWorkerExecutor()));
		suppliers.put(EzyAddExceptionHandler.class, () -> new EzyAddExceptionHandlerImpl(getExceptionHandlersFetcher()));
	}
	
}
