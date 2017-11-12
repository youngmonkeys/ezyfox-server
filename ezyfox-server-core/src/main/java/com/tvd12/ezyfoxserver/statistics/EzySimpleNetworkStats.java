package com.tvd12.ezyfoxserver.statistics;

import lombok.Getter;

@Getter
public class EzySimpleNetworkStats implements EzyNetworkStats {

    protected long readBytes;
    protected long writtenBytes;
    protected long droppedInBytes;
    protected long writeErrorBytes;
    
    protected long readPackets;
    protected long writtenPackets;
    protected long droppedInPackets;
    protected long writeErrorPackets;
    
    @Override
    public void addReadBytes(long bytes) {
        this.readBytes += bytes;
    }

    @Override
    public void addWrittenBytes(long bytes) {
        this.writtenBytes += bytes;
    }

    @Override
    public void addDroppedInBytes(long bytes) {
        this.droppedInBytes += bytes;
    }

    @Override
    public void addWriteErrorBytes(long bytes) {
        this.writeErrorBytes += bytes;
    }

    @Override
    public void addReadPackets(long packets) {
        this.readPackets += packets;
    }

    @Override
    public void addWrittenPackets(long packets) {
        this.writtenPackets += packets;
    }

    @Override
    public void addDroppedInPackets(long packets) {
        this.droppedInPackets += packets;
    }

    @Override
    public void addWriteErrorPackets(long packets) {
        this.writeErrorPackets += packets;
    }

    
    
}
