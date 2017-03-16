package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.command.EzyAddEventController;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.impl.EzyAddEventControllerImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppFireEventImpl;
import com.tvd12.ezyfoxserver.config.EzyApp;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleAppContext 
		extends EzyChildContext 
		implements EzyAppContext {

	@Setter
	@Getter
	protected EzyApp app;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, () -> new EzyAppFireEventImpl(this));
		suppliers.put(EzyAddEventController.class, () -> new EzyAddEventControllerImpl(app));
	}
	
}
