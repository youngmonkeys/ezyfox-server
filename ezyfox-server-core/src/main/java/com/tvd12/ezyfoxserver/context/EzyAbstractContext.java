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
import com.tvd12.ezyfoxserver.command.impl.EzyAddCommandImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAddExceptionHandlerImpl;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractContext 
        extends EzyEntity 
        implements EzyInitable, EzyDestroyable {

	protected Map<Class, Supplier> commandSuppliers;
	
	@Override
	public final void init() {
	    this.commandSuppliers = defaultCommandSuppliers();
	    this.properties.put(
	            EzyAddExceptionHandler.class, 
	            new EzyAddExceptionHandlerImpl(getComponent()));
	    this.properties.put(EzyAddCommand.class, new EzyAddCommandImpl(this));
	    this.init0();
	}
	
	protected void init0() {
	}
	
	protected abstract EzyComponent getComponent();
	
	protected Map<Class, Supplier> defaultCommandSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		addCommandSuppliers(answer);
		return answer;
	}
	
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
	}
	
    public void addCommand(Class commandType, Supplier commandSupplier) {
	    this.commandSuppliers.put(commandType, commandSupplier);
	}
	
	@Override
	public void destroy() {
	    this.properties.clear();
	    this.properties = null;
	    this.commandSuppliers.clear();
	    this.commandSuppliers = null;
	}
	
}
