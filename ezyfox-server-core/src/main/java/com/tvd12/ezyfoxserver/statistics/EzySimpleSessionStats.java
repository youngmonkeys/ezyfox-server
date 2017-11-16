package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class EzySimpleSessionStats implements EzySessionStats, Serializable {
    private static final long serialVersionUID = 8573411975323609800L;

    private int maxSessions;
    private int totalSessions;
    private int currentSessions;
    
    @Override
    public void addSessions(int sessions) {
        this.totalSessions += sessions;
    }

    @Override
    public void setCurrentSessions(int sessions) {
        this.currentSessions = sessions;
        if(sessions > maxSessions)
            this.maxSessions = sessions;
    }

    
    
}
