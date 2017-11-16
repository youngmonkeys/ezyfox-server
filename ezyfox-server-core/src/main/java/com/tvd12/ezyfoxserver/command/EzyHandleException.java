package com.tvd12.ezyfoxserver.command;

public interface EzyHandleException {

    void handle(Thread thread, Throwable throwable);
    
}
