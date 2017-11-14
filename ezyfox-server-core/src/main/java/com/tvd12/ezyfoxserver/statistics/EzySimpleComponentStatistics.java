package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class EzySimpleComponentStatistics 
        implements EzyComponentStatistics, Serializable {
    private static final long serialVersionUID = 5358440557223026711L;
    
    protected EzySessionStats sessionStats = newSessionStats();
    protected EzyNetworkStats networkStats = newNetworkStats();
    
    protected EzySessionStats newSessionStats() {
        return new EzySimpleSessionStats();
    }
    
    protected EzyNetworkStats newNetworkStats() {
        return new EzySimpleNetworkStats();
    }
    
}
