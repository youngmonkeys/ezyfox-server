package com.tvd12.ezyfoxserver.statistics;

import com.tvd12.ezyfox.io.EzyDates;
import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class EzyNetworkBytesFrame implements Serializable {
    private static final long serialVersionUID = 4153904393261840635L;
    protected final long endTime;
    protected final long startTime;
    protected long readBytes;
    protected long writtenBytes;

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
        return getClass().getSimpleName() +
            ": " +
            EzyDates.format(startTime) +
            " -> " +
            EzyDates.format(endTime);
    }
}
