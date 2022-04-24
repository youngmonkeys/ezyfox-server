package com.tvd12.ezyfoxserver.statistics;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class EzySimpleComponentStatistics
    implements EzyComponentStatistics, Serializable {
    private static final long serialVersionUID = 5358440557223026711L;

    protected final EzySessionStats sessionStats = newSessionStats();
    protected final EzyNetworkStats networkStats = newNetworkStats();

    protected EzySessionStats newSessionStats() {
        return new EzySimpleSessionStats();
    }

    protected EzyNetworkStats newNetworkStats() {
        return new EzySimpleNetworkStats();
    }
}
