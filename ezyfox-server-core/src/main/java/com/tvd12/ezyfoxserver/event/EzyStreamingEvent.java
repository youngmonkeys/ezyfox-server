package com.tvd12.ezyfoxserver.event;

public interface EzyStreamingEvent extends EzyUserEvent {

    byte[] getBytes();
    
}
