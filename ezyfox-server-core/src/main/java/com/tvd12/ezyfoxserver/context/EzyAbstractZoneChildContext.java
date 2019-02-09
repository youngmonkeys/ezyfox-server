package com.tvd12.ezyfoxserver.context;

import java.util.concurrent.ScheduledExecutorService;

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
	    if(containsKey(clazz))
            return getProperty(clazz);
		return parent.get(clazz);
	}
	
	@SuppressWarnings("unchecked")
    public <T> T cmd(Class<T> clazz) {
        if(commandSuppliers.containsKey(clazz))
            return (T) commandSuppliers.get(clazz).get();
        return parent.cmd(clazz);
    }
	
	public void setExecutorService(ScheduledExecutorService executorService) {
	    properties.put(ScheduledExecutorService.class, executorService);
	}
}
