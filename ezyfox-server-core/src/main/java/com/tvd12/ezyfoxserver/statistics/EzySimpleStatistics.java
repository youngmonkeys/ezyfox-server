package com.tvd12.ezyfoxserver.statistics;

import lombok.Getter;

@Getter
public class EzySimpleStatistics implements EzyStatistics {

    protected EzySocketStatistics socketStats = newSocketStatistics();
    protected EzyWebSocketStatistics webSocketStats = newWebSocketStatistics();
    
    protected EzySocketStatistics newSocketStatistics() {
        return new EzySimpleSocketStatistics();
    }
    
    protected EzyWebSocketStatistics newWebSocketStatistics() {
        return new EzySimpleWebSocketStatistics();
    }
    
}
