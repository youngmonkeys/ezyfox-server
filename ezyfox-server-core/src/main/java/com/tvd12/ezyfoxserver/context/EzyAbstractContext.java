package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.entity.EzyEntity;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyInitable;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.command.EzyAddCommand;
import com.tvd12.ezyfoxserver.command.EzyAddExceptionHandler;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.impl.EzyAddCommandImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAddExceptionHandlerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyHandleExceptionImpl;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractContext 
        extends EzyEntity 
        implements EzyInitable, EzyDestroyable {

	protected Map<Class, Supplier> commandSuppliers;
	protected EzyHandleException handleException;
	
	@Override
	public final void init() {
	    this.commandSuppliers = defaultCommandSuppliers();
	    this.handleException = new EzyHandleExceptionImpl(getComponent());
	    this.properties.put(EzyHandleException.class, handleException);
	    this.properties.put(EzyAddCommand.class, new EzyAddCommandImpl(this));
	    this.properties.put(EzyAddExceptionHandler.class, new EzyAddExceptionHandlerImpl(getComponent()));
	    this.init0();
	}
	
	protected void init0() {}
	protected abstract EzyComponent getComponent();
	
	public void handleException(Thread thread, Throwable throwable) {
	    this.handleException.handle(thread, throwable);
	}
	
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		addCommandSuppliers(answer);
		return answer;
	}
	
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {}
	
    public void addCommand(Class commandType, Supplier commandSupplier) {
	    this.commandSuppliers.put(commandType, commandSupplier);
	}
	
	@Override
	public void destroy() {
	    this.properties.clear();
	    this.commandSuppliers.clear();
	    this.commandSuppliers = null;
	    this.handleException = null;
	}
	
}
