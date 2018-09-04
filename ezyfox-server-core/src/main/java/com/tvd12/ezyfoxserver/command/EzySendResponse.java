package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzySendResponse extends EzyVoidCommand {

    EzySendResponse immediate(boolean immediate);
    
    EzySendResponse response(EzyResponse response);
    
    EzySendResponse recipient(EzySession recipient);
    
    EzySendResponse recipients(Collection<EzySession> recipients);
    
}
