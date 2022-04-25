package com.tvd12.ezyfoxserver.statistics;

public interface EzyUserStatistics extends EzyUserRoStatistics {

    void addUsers(int users);

    void setCurrentUsers(int users);
}
