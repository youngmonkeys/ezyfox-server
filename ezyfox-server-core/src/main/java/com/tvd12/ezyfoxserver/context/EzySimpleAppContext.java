package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyAddEventController;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.impl.EzyAddEventControllerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppResponseImpl;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleAppContext 
		extends EzyAbstractZoneChildContext 
		implements EzyAppContext {

	@Setter
	@Getter
	protected EzyApplication app;
	
	protected EzyAppSetting getSetting() {
        return app.getSetting();
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, () -> new EzyAppFireEventImpl(this));
		suppliers.put(EzyAppResponse.class, () -> new EzyAppResponseImpl(this));
		suppliers.put(EzyHandleException.class, ()-> new EzyAppHandleExceptionImpl(getApp()));
		suppliers.put(EzyAddEventController.class, () -> new EzyAddEventControllerImpl(getSetting()));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addUnsafeCommands(Set<Class> unsafeCommands) {
		super.addUnsafeCommands(unsafeCommands);
		unsafeCommands.add(EzyFirePluginEvent.class);
		unsafeCommands.add(EzyFireAppEvent.class);
	}
	
	@Override
	protected EzyExceptionHandlersFetcher getExceptionHandlersFetcher() {
	    return (EzyExceptionHandlersFetcher) app;
	}
	
	@Override
	public void destroy() {
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
