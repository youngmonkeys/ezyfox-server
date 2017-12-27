package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;

public class EzySendResponseImpl 
        extends EzyHasSenderCommand<EzySendResponseImpl> 
        implements EzySendResponse {

    protected boolean immediate;
    protected EzyResponse response;
    
    protected final Set<EzyConstant> unloggableCommands;
    protected final EzyResponseSerializer responseSerializer;
    
    public EzySendResponseImpl(EzyServerContext context) {
        this.responseSerializer = context.get(EzyResponseSerializer.class);
        this.unloggableCommands = context.getServer()
                .getSettings().getLogger().getIgnoredCommands().getCommands(); 
    }
    
    @Override
    public Boolean execute() {
        EzyArray data = responseSerializer.serializeToArray(response);
        sendData(data);
        debugLogResponse(data);
        destroy();
        return Boolean.TRUE;
    }
    
    protected void sendData(EzyArray data) {
        try {
            if(immediate)
                sender.sendNow(data);
            else 
                sender.send(data);
        } 
        catch(Exception e) {
            getLogger().error("send data {} to clients error", data);
        }
    }
    
    protected void debugLogResponse(Object data) {
        if(!unloggableCommands.contains(response.getCommand()))
            getLogger().debug("send to: {} data: {}", getSenderName(), data);
    }
    
    protected void destroy() {
        this.sender = null;
        this.response = null;
    }
    
    @Override
    public EzySendResponse immediate(boolean immediate) {
        this.immediate = immediate;
        return this;
    }
    
    @Override
    public EzySendResponse response(EzyResponse response) {
        this.response = response;
        return this;
    }

}
