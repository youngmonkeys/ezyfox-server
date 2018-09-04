package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.command.EzyAppSetup;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.command.impl.EzyAppFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppSetupImpl;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleAppContext 
		extends EzyAbstractZoneChildContext 
		implements EzyAppContext {

	@Setter
	@Getter
	protected EzyApplication app;
	protected EzyFireEvent fireEvent;
	
	@Override
	protected void init0() {
	    EzySetup setup = new EzyAppSetupImpl(app);
	    this.fireEvent = new EzyAppFireEventImpl(this);
	    this.properties.put(EzyFireEvent.class, fireEvent);
	    this.properties.put(EzyHandleException.class, new EzyAppHandleExceptionImpl(app));
	    this.properties.put(EzySetup.class, setup);
	    this.properties.put(EzyAppSetup.class, setup);
	}
	
	@Override
	public void fireEvent(EzyConstant type, EzyEvent event) {
	    this.fireEvent.fire(type, event);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		suppliers.put(EzyAppResponse.class, () -> new EzyAppResponseImpl(this));
	}
	
	@Override
	protected EzyComponent getComponent() {
	    return (EzyComponent) app;
	}
	
	@Override
	public void destroy() {
	    super.destroy();
	    this.destroyApp();
	    this.clearProperties();
	}

	protected void clearProperties() {
	    this.app = null;
	    this.fireEvent = null;
	}
	
	protected void destroyApp() {
	    processWithLogException(( )-> ((EzyDestroyable)app).destroy());
	}
	
	@Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleAppContext>()
                .function(t -> t.app)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(app).hashCode();
    }
	
}
