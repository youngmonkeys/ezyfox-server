package com.tvd12.ezyfoxserver.command.impl;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.entity.EzySender;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public abstract class EzyHasSenderCommand<T extends EzyHasSenderCommand<T>> extends EzyAbstractCommand {

    protected EzySender sender;
    
    @SuppressWarnings("unchecked")
    public T sender(EzySender sender) {
        this.sender = sender;
        return (T)this;
    }
    
    protected String getSenderName() {
        if(sender instanceof EzyUser)
            return ((EzyUser)sender).getName();
        EzySession session = ((EzySession)sender);
        SocketAddress socketAddress = session.getClientAddress();
        return socketAddress != null ? socketAddress.toString() : session.getName();
    }
    
}
