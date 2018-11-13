package com.tvd12.ezyfoxserver.event;

public interface EzyStreamingEvent extends EzyUserSessionEvent {

    byte[] getBytes();
    
}
