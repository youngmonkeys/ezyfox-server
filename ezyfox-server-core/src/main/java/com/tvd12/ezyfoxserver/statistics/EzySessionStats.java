package com.tvd12.ezyfoxserver.statistics;

public interface EzySessionStats extends EzySessionRoStats {

    void addSessions(int sessions);
    
    void setCurrentSessions(int sessions);
    
}
