package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
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
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractContext 
        extends EzyEntity 
        implements EzyInitable, EzyDestroyable {

    protected EzyComponent component;
	protected Map<Class, Supplier> commandSuppliers;
	protected EzyHandleException handleException;
	
	@Override
	public final void init() {
	    this.commandSuppliers = defaultCommandSuppliers();
	    this.handleException = new EzyHandleExceptionImpl(component);
	    this.properties.put(EzyHandleException.class, handleException);
	    this.properties.put(EzyAddCommand.class, new EzyAddCommandImpl(this));
	    this.properties.put(EzyAddExceptionHandler.class, new EzyAddExceptionHandlerImpl(component));
	    this.init0();
	}
	
	protected void init0() {}
	
	@SuppressWarnings("unchecked")
    public void handleEvent(EzyConstant eventType, EzyEvent event) {
	    EzyEventControllers controllers = component.getEventControllers();
	    EzyEventController controller = controllers.getController(eventType);
	    if(controller != null)
	        controller.handle(this, event);
	}
	
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
	    this.component = null;
	    this.commandSuppliers = null;
	    this.handleException = null;
	}
	
}
