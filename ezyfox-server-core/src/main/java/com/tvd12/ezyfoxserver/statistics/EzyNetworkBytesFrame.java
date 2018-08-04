package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfox.io.EzyDates;

import lombok.Getter;

@Getter
public abstract class EzyNetworkBytesFrame implements Serializable {
    private static final long serialVersionUID = 4153904393261840635L;
    protected long endTime;
    protected long startTime;
    protected long readBytes;
    protected long writtenBytes;
    protected long id = COUNTER.incrementAndGet();
    
    private static final AtomicLong COUNTER  = new AtomicLong(0);
    
    public EzyNetworkBytesFrame() {
        this(System.currentTimeMillis());
    }
    
    public EzyNetworkBytesFrame(long startTime) {
        this.startTime = startTime;
        this.endTime = startTime + getExistsTime();
    }
    
    protected abstract long getExistsTime();
    
    public void addReadBytes(long bytes) {
        this.readBytes += bytes;
    }
    
    public void addWrittenBytes(long bytes) {
        this.writtenBytes += bytes;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > endTime;
    }
    
    public abstract EzyNetworkBytesFrame nextFrame();
    
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
