package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.command.EzyAddEventController;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.impl.EzyAddEventControllerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginResponseImpl;

import lombok.Getter;
import lombok.Setter;


public class EzySimplePluginContext 
		extends EzyAbstractZoneChildContext 
		implements EzyPluginContext {

	@Setter
	@Getter
	protected EzyPlugin plugin;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, () -> new EzyPluginFireEventImpl(this));
		suppliers.put(EzyPluginResponse.class, () -> new EzyPluginResponseImpl(this));
		suppliers.put(EzyHandleException.class, ()-> new EzyPluginHandleExceptionImpl(getPlugin()));
		suppliers.put(EzyAddEventController.class, () -> new EzyAddEventControllerImpl(getPlugin()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void addUnsafeCommands(Set<Class> unsafeCommands) {
		super.addUnsafeCommands(unsafeCommands);
		unsafeCommands.add(EzyFirePluginEvent.class);
	}
	
	@Override
	protected EzyExceptionHandlersFetcher getExceptionHandlersFetcher() {
	    return (EzyExceptionHandlersFetcher) plugin;
	}

    @Override
    public void destroy() {
        processWithLogException(( )-> ((EzyDestroyable)plugin).destroy());
    }
    
    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimplePluginContext>()
                .function(t -> t.plugin)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(plugin).hashCode();
    }
	
}
