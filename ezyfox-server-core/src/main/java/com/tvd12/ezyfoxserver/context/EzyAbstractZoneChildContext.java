package com.tvd12.ezyfoxserver.context;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.concurrent.EzyExecutorServiceFetcher;

import lombok.Getter;
import lombok.Setter;

@Setter 
@Getter
public abstract class EzyAbstractZoneChildContext 
        extends EzyAbstractContext 
        implements EzyExecutorServiceFetcher {

	protected EzyZoneContext parent;
    protected ScheduledExecutorService executorService;
	
	public <T> T get(Class<T> clazz) {
		T property = getProperty(clazz);
	    if(property != null)
            return property;
		return parent.get(clazz);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T cmd(Class<T> clazz) {
		Supplier supplier = commandSuppliers.get(clazz);
        if(supplier != null)
            return (T) supplier.get();
        return parent.cmd(clazz);
    }
	
	public void setExecutorService(ScheduledExecutorService executorService) {
	    properties.put(ScheduledExecutorService.class, executorService);
	}
}
