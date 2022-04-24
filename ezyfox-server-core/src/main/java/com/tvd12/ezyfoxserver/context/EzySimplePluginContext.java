package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.EzyPluginSendResponse;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.command.EzySetup;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginSendResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginSetupImpl;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Getter;


public class EzySimplePluginContext 
        extends EzyAbstractZoneChildContext
        implements EzyPluginContext {

    @Getter
    protected EzyPlugin plugin;
    protected EzyPluginSendResponse sendResponse;

    @Override
    protected void init0() {
        EzySetup setup = new EzyPluginSetupImpl(plugin);
        this.sendResponse = new EzyPluginSendResponseImpl(this);
        this.properties.put(EzyPluginSendResponse.class, sendResponse);
        this.properties.put(EzyHandleException.class, new EzyPluginHandleExceptionImpl(plugin));
        this.properties.put(EzySetup.class, setup);
        this.properties.put(EzyPluginSetup.class, setup);
    }

    @Override
    public void send(
            EzyData data,
            EzySession recipient,
            boolean encrypted, EzyTransportType transportType) {
        this.sendResponse.execute(data, recipient, encrypted, transportType);
    }

    @Override
    public void send(
            EzyData data,
            Collection<EzySession> recipients,
            boolean encrypted, EzyTransportType transportType) {
        this.sendResponse.execute(data, recipients, encrypted, transportType);
    }

    public void setPlugin(EzyPlugin plugin) {
        this.plugin = plugin;
        this.component = (EzyComponent)plugin;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
        suppliers.put(EzyPluginResponse.class, () -> new EzyPluginResponseImpl(this));
    }

    @Override
    public void destroy() {
        super.destroy();
        this.destroyPlugin();
        this.clearProperties();
    }
    
    protected void clearProperties() {
        this.plugin = null;
        this.sendResponse = null;
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
        return new EzyHashCodes().append(plugin).toHashCode();
    }
    
    @Override
    protected void preDestroy() {
        logger.debug("destroy PluginContext({})", plugin);
    }
    
}
