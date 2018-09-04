package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginSetupImpl;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.Getter;
import lombok.Setter;


public class EzySimplePluginContext 
		extends EzyAbstractZoneChildContext 
		implements EzyPluginContext {

	@Setter
	@Getter
	protected EzyPlugin plugin;
	protected EzyFireEvent fireEvent;
	
	@Override
	protected void init0() {
	    this.fireEvent = new EzyPluginFireEventImpl(this);
	    this.properties.put(EzyFireEvent.class, fireEvent);
	    this.properties.put(EzyHandleException.class, new EzyPluginHandleExceptionImpl(plugin));
	    this.properties.put(EzyPluginSetup.class, new EzyPluginSetupImpl(plugin));
	}
	
	@Override
	public void fireEvent(EzyConstant type, EzyEvent event) {
	    this.fireEvent.fire(type, event);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		suppliers.put(EzyPluginResponse.class, () -> new EzyPluginResponseImpl(this));
	}

	@Override
	protected EzyComponent getComponent() {
	    return (EzyComponent) plugin;
	}
	
    @Override
    public void destroy() {
        super.destroy();
        this.destroyPlugin();
        this.clearProperties();
    }
    
    protected void clearProperties() {
        this.plugin = null;
        this.fireEvent = null;
    }
    
    protected void destroyPlugin() {
        processWithLogException(()-> ((EzyDestroyable)plugin).destroy());
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
