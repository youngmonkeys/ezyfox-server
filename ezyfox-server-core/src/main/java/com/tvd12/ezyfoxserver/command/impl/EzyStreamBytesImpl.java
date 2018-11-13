package com.tvd12.ezyfoxserver.command.impl;

import java.util.Collection;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyStreamBytes;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzySimpleBytesPackage;

public class EzyStreamBytesImpl extends EzyAbstractCommand implements EzyStreamBytes {

    protected final EzyServer server;
    
    public EzyStreamBytesImpl(EzyServer server) {
        this.server = server;
    }
    
    @Override
    public void execute(byte[] bytes, EzySession recipient) {
        boolean success = false;
        EzyStreamingApi streamingApi = server.getStreamingApi();
        EzySimpleBytesPackage pack = newPackage(bytes);
        pack.addRecipient(recipient);
        try {
            streamingApi.response(pack);
            success = true;
        } 
        catch(Exception e) {
            logger.error("send {} bytes " + bytes.length + ", to client: " + recipient.getName() + " error", e);
        }
        finally {
            pack.release();
        }
        if(success)
            logger.debug("send {} bytes to: {}", bytes.length, recipient.getName());
    }
    
    @Override
    public void execute(byte[] bytes, Collection<EzySession> recipients) {
        boolean success = false;
        EzyStreamingApi streamingApi = server.getStreamingApi();
        EzySimpleBytesPackage pack = newPackage(bytes);
        pack.addRecipients(recipients);
        try {
            streamingApi.response(pack);
            success = true;
        } 
        catch(Exception e) {
            logger.error("send {} bytes " + bytes.length + ", to client: " + getRecipientsNames(recipients) + " error", e);
        }
        finally {
            pack.release();
        }
        if(success)
            logger.debug("send {} bytes to: {}", bytes.length, getRecipientsNames(recipients));
    }
    
    protected EzySimpleBytesPackage newPackage(byte[] bytes) {
        EzySimpleBytesPackage pack = new EzySimpleBytesPackage();
        pack.setBytes(bytes);
        return pack;
    }
    
    protected String getRecipientsNames(Collection<EzySession> recipients) {
        StringBuilder builder = new StringBuilder()
                .append("[ ");
        for(EzySession recv : recipients)
            builder.append(recv.getName()).append(" ");
        return builder.append("]").toString();
    }
    
}
