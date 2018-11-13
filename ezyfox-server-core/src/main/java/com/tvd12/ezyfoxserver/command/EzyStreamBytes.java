package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyStreamBytes {

    void execute(byte[] bytes, EzySession recipient);
    
    void execute(byte[] bytes, Collection<EzySession> recipients);
    
}
