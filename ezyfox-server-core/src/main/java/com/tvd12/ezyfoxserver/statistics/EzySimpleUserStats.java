package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class EzySimpleUserStats implements EzyUserStatistics, Serializable {
    private static final long serialVersionUID = 8573411975323609800L;

    protected int maxUsers;
    protected int totalUsers;
    protected int currentUsers;
    
    @Override
    public void addUsers(int users) {
        this.totalUsers += users;
    }

    @Override
    public void setCurrentUsers(int users) {
        this.currentUsers = users;
        if(users > maxUsers)
            this.maxUsers = users;
    }

    
    
}
