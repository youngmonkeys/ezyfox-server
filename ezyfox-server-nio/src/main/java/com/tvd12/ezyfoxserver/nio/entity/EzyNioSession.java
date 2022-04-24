package com.tvd12.ezyfoxserver.nio.entity;

import java.nio.channels.SelectionKey;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyNioSession extends EzySession {

    String SELECTION_KEY    = "SessionSelectionKey";
    
    SelectionKey getSelectionKey();
    
}
