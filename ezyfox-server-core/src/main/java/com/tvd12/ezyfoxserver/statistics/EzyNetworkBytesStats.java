package com.tvd12.ezyfoxserver.statistics;

public interface EzyNetworkBytesStats extends EzyNetworkRoBytesStats {

    void addReadBytes(long bytes);

    void addWrittenBytes(long bytes);

    void addDroppedInBytes(long bytes);

    void addDroppedOutBytes(long bytes);

    void addWriteErrorBytes(long bytes);

}
