package com.tvd12.ezyfoxserver.statistics;

public interface EzyNetworkRoPacketsStats {

    long getReadPackets();
    
    long getWrittenPackets();

    long getDroppedInPackets();
    
    long getWriteErrorPackets();
    
}
