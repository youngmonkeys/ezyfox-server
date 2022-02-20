package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;

import com.tvd12.ezyfox.io.EzyDates;

import lombok.Getter;

@Getter
public abstract class EzyRequestFrame implements Serializable {
    private static final long serialVersionUID = 5034914725676324216L;

    protected volatile int requests;
    protected final long endTime;
    protected final long startTime;
    protected final int maxRequests;
    
    public EzyRequestFrame(int maxRequests) {
        this(maxRequests, System.currentTimeMillis());
    }
    
    public EzyRequestFrame(int maxRequests, long startTime) {
        this.maxRequests = maxRequests;
        this.startTime = startTime;
        this.endTime = startTime + getExistsTime();
    }
    
    protected abstract long getExistsTime();
    
    public boolean addRequests(long requests) {
        return (this.requests += requests) > maxRequests;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > endTime;
    }
    
    public boolean isInvalid() {
        return requests > maxRequests;
    }
    
    public abstract EzyRequestFrame nextFrame();
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append(getClass().getSimpleName())
                .append(": ")
                .append(EzyDates.format(startTime))
                .append(" -> ")
                .append(EzyDates.format(endTime))
                .toString();
    }
    
}
