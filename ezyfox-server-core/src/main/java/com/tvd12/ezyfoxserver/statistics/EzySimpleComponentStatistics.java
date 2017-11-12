package com.tvd12.ezyfoxserver.statistics;

import lombok.Getter;

@Getter
public class EzySimpleComponentStatistics implements EzyComponentStatistics {

    protected EzyNetworkStats networkStats = newNetworkStats();
    
    protected EzyNetworkStats newNetworkStats() {
        return new EzySimpleNetworkStats();
    }
    
}
