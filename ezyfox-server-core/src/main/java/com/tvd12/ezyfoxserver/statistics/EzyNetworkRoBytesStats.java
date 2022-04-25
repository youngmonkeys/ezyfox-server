package com.tvd12.ezyfoxserver.statistics;

public interface EzyNetworkRoBytesStats {

    long getReadBytes();

    long getWrittenBytes();

    long getDroppedInBytes();

    long getWriteErrorBytes();

    long getReadBytesPerHour();

    long getReadBytesPerMinute();

    long getReadBytesPerSecond();

    long getWrittenBytesPerHour();

    long getWrittenBytesPerMinute();

    long getWrittenBytesPerSecond();
}
