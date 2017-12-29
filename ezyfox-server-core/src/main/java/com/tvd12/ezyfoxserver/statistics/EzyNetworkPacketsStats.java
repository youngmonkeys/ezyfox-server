package com.tvd12.ezyfoxserver.statistics;

public interface EzyNetworkPacketsStats extends EzyNetworkRoPacketsStats {

    void addReadPackets(long packets);
    
    void addWrittenPackets(long packets);

    void addDroppedInPackets(long packets);
    
    void addDroppedOutPackets(long packets);
    
    void addWriteErrorPackets(long packets);
    
}
