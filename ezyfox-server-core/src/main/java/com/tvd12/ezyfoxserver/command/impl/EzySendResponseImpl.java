package com.tvd12.ezyfoxserver.command.impl;

import java.net.SocketAddress;
import java.util.Set;

import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySender;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyLoggerSetting;

public class EzySendResponseImpl extends EzyAbstractCommand implements EzySendResponse {

    protected EzySender sender;
    protected boolean immediate;
    protected EzyResponse response;

    protected final EzyLoggerSetting loggerSetting;
    protected final Set<EzyConstant> unloggableCommands;
    
    public EzySendResponseImpl(EzyServerContext context) {
        this.loggerSetting = context.getServer().getSettings().getLogger();
        this.unloggableCommands = loggerSetting.getIgnoredCommands().getCommands(); 
    }
    
    @Override
    public EzySendResponse sender(EzySender sender) {
        this.sender = sender;
        return this;
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
    
    @Override
    public Boolean execute() {
        EzyArray data = response.serialize();
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
        this.response.release();
        this.sender = null;
        this.response = null;
    }
    
    protected String getSenderName() {
        if(sender instanceof EzyUser)
            return ((EzyUser)sender).getName();
        EzySession session = ((EzySession)sender);
        SocketAddress socketAddress = session.getClientAddress();
        return socketAddress != null ? socketAddress.toString() : session.getName();
    }
    
}
