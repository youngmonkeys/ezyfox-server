package com.tvd12.ezyfoxserver.statistics;

public interface EzyStatistics {

    long getStartTime();
    
    EzySocketStatistics getSocketStats();
    
    EzyWebSocketStatistics getWebSocketStats();
    
}
