package com.tvd12.ezyfoxserver.statistics;

import com.tvd12.ezyfox.io.EzyDates;
import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class EzyRequestFrame implements Serializable {
    private static final long serialVersionUID = 5034914725676324216L;
    protected final long endTime;
    protected final long startTime;
    protected final int maxRequests;
    protected volatile int requests;

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
        //noinspection NonAtomicOperationOnVolatileField
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
        return getClass().getSimpleName() +
            ": " +
            EzyDates.format(startTime) +
            " -> " +
            EzyDates.format(endTime);
    }
}
