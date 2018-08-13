package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.command.impl.EzyCloseSessionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyServerHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyServerShutdown;
import com.tvd12.ezyfoxserver.exception.EzyZoneNotFoundException;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleServerContext extends EzyAbstractComplexContext implements EzyServerContext {

	@Setter
	@Getter
	protected EzyServer server;
	
	protected Map<Integer, EzyZoneContext> zoneContextsById = new ConcurrentHashMap<>();
    protected Map<String, EzyZoneContext> zoneContextsByName = new ConcurrentHashMap<>();
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz).get();
		if(containsKey(clazz))
			return getProperty(clazz);
		throw new IllegalArgumentException("has no instance of " + clazz);
	}
	
	public void addZoneContexts(Collection<EzyZoneContext> zoneContexts) {
        for(EzyZoneContext ctx : zoneContexts)
            addZoneContext(ctx.getZone().getSetting(), ctx);
    }
	
	public void addZoneContext(EzyZoneSetting zone, EzyZoneContext zoneContext) {
	    zoneContextsById.put(zone.getId(), zoneContext);
	    zoneContextsByName.put(zone.getName(), zoneContext);
	    addAppContexts(((EzyAppContextsFetcher)zoneContext).getAppContexts());
	    addPluginContexts(((EzyPluginContextsFetcher)zoneContext).getPluginContexts());
	}
	
	@Override
	public EzyZoneContext getZoneContext(int zoneId) {
	    if(zoneContextsById.containsKey(zoneId))
	        return zoneContextsById.get(zoneId);
	    throw new EzyZoneNotFoundException(zoneId);
	}
	
	@Override
	public EzyZoneContext getZoneContext(String zoneName) {
	    if(zoneContextsByName.containsKey(zoneName))
	        return zoneContextsByName.get(zoneName);
	    throw new EzyZoneNotFoundException(zoneName);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, ()-> new EzyFireEventImpl(this));
		suppliers.put(EzyShutdown.class, ()-> new EzyServerShutdown(this));
		suppliers.put(EzySendResponse.class, ()-> new EzySendResponseImpl(this));
		suppliers.put(EzyCloseSession.class, ()-> new EzyCloseSessionImpl(this));
		suppliers.put(EzyHandleException.class, ()-> new EzyServerHandleExceptionImpl(getServer()));
	}
	
	@Override
	protected EzyExceptionHandlersFetcher getExceptionHandlersFetcher() {
	    return (EzyExceptionHandlersFetcher) server;
	}
	
	@Override
	public void destroy() {
	    destroyServer();
	    destroyZoneContexts();
	}
	
	private void destroyServer() {
	    processWithLogException(() -> ((EzyDestroyable)server).destroy());
	}
	
	private void destroyZoneContexts() {
        zoneContextsById.values().forEach(this::destroyZoneContext);
    }
	
	private void destroyZoneContext(EzyZoneContext zoneContext) {
	    processWithLogException(() -> ((EzyDestroyable)zoneContext).destroy());
	}
	
}
