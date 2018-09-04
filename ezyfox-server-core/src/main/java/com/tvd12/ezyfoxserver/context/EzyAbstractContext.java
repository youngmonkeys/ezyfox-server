package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.entity.EzyEntity;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyInitable;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.command.EzyAddExceptionHandler;
import com.tvd12.ezyfoxserver.command.impl.EzyAddExceptionHandlerImpl;

public abstract class EzyAbstractContext 
        extends EzyEntity 
        implements EzyInitable, EzyDestroyable {

	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> commandSuppliers;
	
	@Override
	public final void init() {
	    this.commandSuppliers = defaultCommandSuppliers();
	    this.properties.put(
	            EzyAddExceptionHandler.class, 
	            new EzyAddExceptionHandlerImpl(getComponent()));
	    this.init0();
	}
	
	protected void init0() {
	}
	
	protected abstract EzyComponent getComponent();
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		addCommandSuppliers(answer);
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
	}
	
	@Override
	public void destroy() {
	    this.properties.clear();
	    this.properties = null;
	    this.commandSuppliers.clear();
	    this.commandSuppliers = null;
	}
	
}
