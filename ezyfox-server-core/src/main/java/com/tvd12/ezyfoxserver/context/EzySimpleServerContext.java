package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyBroadcastEvent;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.command.EzyStreamBytes;
import com.tvd12.ezyfoxserver.command.impl.EzyBroadcastEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyCloseSessionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyServerShutdownImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyStreamBytesImpl;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.exception.EzyZoneNotFoundException;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;

import lombok.Getter;

public class EzySimpleServerContext extends EzyAbstractComplexContext implements EzyServerContext {

	@Getter
	protected EzyServer server;
	protected EzyStreamBytes streamBytes;
	protected EzySendResponse sendResponse;
	protected EzyBroadcastEvent broadcastEvent;
	
	@Getter
	protected final List<EzyZoneContext> zoneContexts = new ArrayList<>();
	protected final Map<Integer, EzyZoneContext> zoneContextsById = new ConcurrentHashMap<>();
    protected final Map<String, EzyZoneContext> zoneContextsByName = new ConcurrentHashMap<>();
	
	
    @Override
    protected void init0() {
        this.broadcastEvent = new EzyBroadcastEventImpl(this);
        this.streamBytes = new EzyStreamBytesImpl(server);
        this.sendResponse = new EzySendResponseImpl(server);
        this.properties.put(EzyBroadcastEvent.class, broadcastEvent);
        this.properties.put(EzyStreamBytes.class, streamBytes);
        this.properties.put(EzySendResponse.class, sendResponse);
        this.properties.put(EzyShutdown.class, new EzyServerShutdownImpl(this));
        this.properties.put(EzyCloseSession.class, new EzyCloseSessionImpl(this));
    }
    
	@Override
	public <T> T get(Class<T> clazz) {
	    T property = getProperty(clazz);
	    if(property != null)
            return property;
		throw new IllegalArgumentException("has no instance of " + clazz);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public <T> T cmd(Class<T> clazz) {
	    Supplier supplier = commandSuppliers.get(clazz);
	    if(supplier != null)
            return (T) supplier.get();
        throw new IllegalArgumentException("has no command of " + clazz);
	}
	
	@Override
	public void broadcast(
	        EzyConstant eventType, EzyEvent event, boolean catchException) {
	    broadcastEvent.fire(eventType, event, catchException);
	}
	
	@Override
	public void send(
	        EzyResponse response, 
	        EzySession recipient, 
	        boolean immediate, EzyTransportType transportType) {
	    sendResponse.execute(response, recipient, immediate, transportType);
	}
	
	@Override
	public void send(EzyResponse response, 
	        Collection<EzySession> recipients, 
	        boolean immediate, EzyTransportType transportType) {
	    sendResponse.execute(response, recipients, immediate, transportType);
	}
	
	@Override
	public void stream(
	        byte[] bytes, 
	        EzySession recipient, EzyTransportType transportType) {
	    streamBytes.execute(bytes, recipient, transportType);
	}
	
	@Override
	public void stream(
	        byte[] bytes, 
	        Collection<EzySession> recipients, EzyTransportType transportType) {
	    streamBytes.execute(bytes, recipients, transportType);
	}
	
	public void addZoneContexts(Collection<EzyZoneContext> zoneContexts) {
        for(EzyZoneContext ctx : zoneContexts)
            addZoneContext(ctx.getZone().getSetting(), ctx);
    }
	
	public void addZoneContext(EzyZoneSetting zone, EzyZoneContext zoneContext) {
	    zoneContexts.add(zoneContext);
	    zoneContextsById.put(zone.getId(), zoneContext);
	    zoneContextsByName.put(zone.getName(), zoneContext);
	    addAppContexts(((EzyAppContextsFetcher)zoneContext).getAppContexts());
	    addPluginContexts(((EzyPluginContextsFetcher)zoneContext).getPluginContexts());
	}
	
	@Override
	public EzyZoneContext getZoneContext(int zoneId) {
	    EzyZoneContext zoneContext = zoneContextsById.get(zoneId);
	    if(zoneContext != null)
	        return zoneContext;
	    throw new EzyZoneNotFoundException(zoneId);
	}
	
	@Override
	public EzyZoneContext getZoneContext(String zoneName) {
	    EzyZoneContext zoneContext = zoneContextsByName.get(zoneName);
	    if(zoneContext != null)
	        return zoneContext;
	    throw new EzyZoneNotFoundException(zoneName);
	}
	
	public void setServer(EzyServer server) {
        this.server = server;
        this.component = (EzyComponent)server;
    }
	
	@Override
	protected void destroyComponents() {
	    destroyZoneContexts();
	    destroyServer();
	}
	
	@Override
	protected void clearProperties() {
	    super.clearProperties();
	    this.server = null;
	    this.sendResponse = null;
	    this.broadcastEvent = null;
	    this.zoneContexts.clear();
	    this.zoneContextsById.clear();
	    this.zoneContextsByName.clear();
	}
	
	private void destroyServer() {
	    processWithLogException(() -> ((EzyDestroyable)server).destroy());
	}
	
	private void destroyZoneContexts() {
	    for(EzyZoneContext zc : zoneContextsById.values())
	        this.destroyZoneContext(zc);
    }
	
	private void destroyZoneContext(EzyZoneContext zoneContext) {
	    processWithLogException(() -> ((EzyDestroyable)zoneContext).destroy());
	}
	
}
