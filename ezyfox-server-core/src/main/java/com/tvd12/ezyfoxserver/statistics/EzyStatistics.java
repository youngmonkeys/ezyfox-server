package com.tvd12.ezyfoxserver.statistics;

public interface EzyStatistics {

    EzySocketStatistics getSocketStats();
    
    EzyWebSocketStatistics getWebSocketStats();
    
}
