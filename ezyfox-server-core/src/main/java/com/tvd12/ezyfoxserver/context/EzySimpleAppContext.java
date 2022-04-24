package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.command.*;
import com.tvd12.ezyfoxserver.command.impl.EzyAppHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppSendResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyAppSetupImpl;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySimpleAppContext
    extends EzyAbstractZoneChildContext
    implements EzyAppContext {

    @Getter
    protected EzyApplication app;
    protected EzyAppSendResponse sendResponse;

    @Override
    protected void init0() {
        EzySetup setup = new EzyAppSetupImpl(app);
        this.sendResponse = new EzyAppSendResponseImpl(this);
        this.properties.put(EzyAppSendResponse.class, sendResponse);
        this.properties.put(EzyHandleException.class, new EzyAppHandleExceptionImpl(app));
        this.properties.put(EzySetup.class, setup);
        this.properties.put(EzyAppSetup.class, setup);
    }

    @Override
    public void send(
        EzyData data,
        EzySession recipient,
        boolean encrypted,
        EzyTransportType transportType
    ) {
        this.sendResponse.execute(data, recipient, encrypted, transportType);
    }

    @Override
    public void send(
        EzyData data,
        Collection<EzySession> recipients,
        boolean encrypted,
        EzyTransportType transportType
    ) {
        this.sendResponse.execute(data, recipients, encrypted, transportType);
    }

    public void setApp(EzyApplication app) {
        this.app = app;
        this.component = (EzyComponent) app;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
        suppliers.put(EzyAppResponse.class, () -> new EzyAppResponseImpl(this));
    }

    @Override
    public void destroy() {
        super.destroy();
        this.destroyApp();
        this.clearProperties();
    }

    protected void clearProperties() {
        this.app = null;
        this.sendResponse = null;
    }

    protected void destroyApp() {
        processWithLogException(() -> ((EzyDestroyable) app).destroy());
    }

    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleAppContext>()
            .function(t -> t.app)
            .isEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return new EzyHashCodes().append(app).toHashCode();
    }

    @Override
    protected void preDestroy() {
        logger.debug("destroy AppContext({})", app);
    }
}
