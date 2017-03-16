package com.tvd12.ezyfoxserver.context;

import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.impl.EzyFirePluginEventImpl;
import com.tvd12.ezyfoxserver.config.EzyPlugin;

import lombok.Getter;
import lombok.Setter;

public class EzySimplePluginContext 
		extends EzyChildContext 
		implements EzyPluginContext {

	@Setter
	@Getter
	protected EzyPlugin plugin;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, () -> new EzyFirePluginEventImpl(this));
	}

	
}
