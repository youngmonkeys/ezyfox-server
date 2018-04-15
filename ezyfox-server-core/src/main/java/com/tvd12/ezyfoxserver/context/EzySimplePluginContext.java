package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

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
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.Getter;
import lombok.Setter;
import static com.tvd12.ezyfoxserver.util.EzyProcessor.*;


public class EzySimplePluginContext 
		extends EzyAbstractZoneChildContext 
		implements EzyPluginContext {

	@Setter
	@Getter
	protected EzyPlugin plugin;
	
	protected EzyPluginSetting getSetting() {
        return plugin.getSetting();
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, () -> new EzyPluginFireEventImpl(this));
		suppliers.put(EzyPluginResponse.class, () -> new EzyPluginResponseImpl(this));
		suppliers.put(EzyHandleException.class, ()-> new EzyPluginHandleExceptionImpl(getPlugin()));
		suppliers.put(EzyAddEventController.class, () -> new EzyAddEventControllerImpl(getSetting()));
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
