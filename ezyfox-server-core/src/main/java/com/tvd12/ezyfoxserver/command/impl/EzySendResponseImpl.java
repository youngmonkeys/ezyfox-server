package com.tvd12.ezyfoxserver.command.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.constant.EzyHasName;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyDeliver;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.response.EzySimplePackage;
import com.tvd12.ezyfoxserver.setting.EzyLoggerSetting;

public class EzySendResponseImpl extends EzyAbstractCommand implements EzySendResponse {

    protected boolean immediate;
    protected EzyResponse response;
    protected List<EzySession> recipients;

    protected final EzyServer server;
    protected final EzyResponseApi responseApi;
    protected final EzyLoggerSetting loggerSetting;
    protected final Set<EzyConstant> unloggableCommands;
    
    public EzySendResponseImpl(EzyServerContext context) {
        this.recipients = new ArrayList<>();
        this.server = context.getServer();
        this.responseApi = server.getResponseApi();
        this.loggerSetting = server.getSettings().getLogger();
        this.unloggableCommands = loggerSetting.getIgnoredCommands().getCommands(); 
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
    public EzySendResponse recipient(EzySession recipient) {
        this.recipients.add(recipient);
        return this;
    }
    
    @Override
    public EzySendResponse recipients(Collection<EzySession> recipients) {
        this.recipients.addAll(recipients);
        return this;
    }
    
    @Override
    public void execute() {
        EzyArray data = response.serialize();
        EzyPackage pack = newPackage(data);
        response(pack);
        debugLogResponse(data);
        destroy();
    }
    
    protected EzyPackage newPackage(EzyArray data) {
        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.addRecipients(recipients);
        return pack;
    }
    
    protected void response(EzyPackage pack) {
        try {
            responseApi.response(pack, immediate);
        } 
        catch(Exception e) {
            getLogger().error("send data: " + pack.getData() + ", to clients: " + getRecipientsNames() + " error");
        }
        finally {
            pack.release();
        }
    }
    
    protected void debugLogResponse(Object data) {
        if(!unloggableCommands.contains(response.getCommand()))
            getLogger().debug("send to: {} data: {}", getRecipientsNames(), data);
    }
    
    protected void destroy() {
        this.recipients.clear();
        this.response.release();
        this.response = null;
        this.recipients = null;
    }
    
    protected String getRecipientsNames() {
        StringBuilder builder = new StringBuilder()
                .append("[ ");
        for(EzyDeliver recv : recipients)
            builder.append(((EzyHasName)recv).getName()).append(" ");
        return builder.append("]").toString();
    }
    
}
