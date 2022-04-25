package com.tvd12.ezyfoxserver.statistics;

public interface EzyStatistics {

    long getStartTime();

    EzyUserStatistics getUserStats();

    EzySocketStatistics getSocketStats();

    EzyWebSocketStatistics getWebSocketStats();
}
