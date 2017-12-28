package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfoxserver.io.EzyDates;

import lombok.Getter;

@Getter
public abstract class EzyRequestFrame implements Serializable {
    private static final long serialVersionUID = 5034914725676324216L;

    protected long endTime;
    protected long startTime;
    protected int maxRequests;
    protected volatile int requests;
    protected long id = COUNTER.incrementAndGet();
    
    private static final AtomicLong COUNTER  = new AtomicLong(0);
    
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
                .append(id)
                .append(" : ")
                .append(EzyDates.format(startTime))
                .append(" -> ")
                .append(EzyDates.format(endTime))
                .toString();
    }
    
}
