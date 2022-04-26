package com.tvd12.ezyfoxserver.nio.entity;

import com.tvd12.ezyfoxserver.entity.EzySession;

import java.nio.channels.SelectionKey;

public interface EzyNioSession extends EzySession {

    String SELECTION_KEY = "SessionSelectionKey";

    SelectionKey getSelectionKey();
}
