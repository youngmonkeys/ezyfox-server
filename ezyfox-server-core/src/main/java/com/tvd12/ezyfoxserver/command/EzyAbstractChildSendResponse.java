package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;

public abstract class EzyAbstractChildSendResponse<C extends EzyZoneChildContext> 
        extends EzyMessageController {
    
    protected final C context;
    protected final EzyZoneContext zoneContext;
    protected final EzyServerContext serverContext;
    
    public EzyAbstractChildSendResponse(C context) {
        this.context = context;
        this.zoneContext = context.getParent();
        this.serverContext = zoneContext.getParent();
    }
}
