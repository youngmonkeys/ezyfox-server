package com.tvd12.ezyfoxserver.statistics;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class EzySimpleNetworkStats implements EzyNetworkStats, Serializable {
    private static final long serialVersionUID = 6439136315133894536L;
    
    protected long readBytes;
    protected long writtenBytes;
    protected long droppedInBytes;
    protected long droppedOutBytes;
    protected long writeErrorBytes;
    
    protected long readPackets;
    protected long writtenPackets;
    protected long droppedInPackets;
    protected long droppedOutPackets;
    protected long writeErrorPackets;

    protected EzyNetworkBytesFrame frameHour = new EzyNetworkBytesFrameHour();
    protected EzyNetworkBytesFrame frameMinute = new EzyNetworkBytesFrameMinute();
    protected EzyNetworkBytesFrame frameSecond = new EzyNetworkBytesFrameSecond();
    
    @Override
    public void addReadBytes(long bytes) {
        this.readBytes += bytes;
        this.addReadBytesToFrames(bytes);
    }
    
    private void addReadBytesToFrames(long bytes) {
        if(frameSecond.isExpired())
            frameSecond = frameSecond.nextFrame();
        frameSecond.addReadBytes(bytes);
        
        if(frameMinute.isExpired())
            frameMinute = frameMinute.nextFrame();
        frameMinute.addReadBytes(bytes);
        
        if(frameHour.isExpired())
            frameHour = frameHour.nextFrame();
        frameHour.addReadBytes(bytes);
    }

    @Override
    public void addWrittenBytes(long bytes) {
        this.writtenBytes += bytes;
        this.addWrittenBytesToFrames(bytes);
    }
    
    private void addWrittenBytesToFrames(long bytes) {
        if(frameSecond.isExpired())
            frameSecond = frameSecond.nextFrame();
        frameSecond.addWrittenBytes(bytes);
        
        if(frameMinute.isExpired())
            frameMinute = frameMinute.nextFrame();
        frameMinute.addWrittenBytes(bytes);
        
        if(frameHour.isExpired())
            frameHour = frameHour.nextFrame();
        frameHour.addWrittenBytes(bytes);
    }

    @Override
    public void addDroppedInBytes(long bytes) {
        this.droppedInBytes += bytes;
    }
    
    @Override
    public void addDroppedOutBytes(long bytes) {
        this.droppedOutBytes += bytes;
    }
    
    @Override
    public void addWriteErrorBytes(long bytes) {
        this.writeErrorBytes += bytes;
    }
    
    @Override
    public long getReadBytesPerHour() {
        return frameHour.getReadBytes();
    }
    
    @Override
    public long getReadBytesPerMinute() {
        return frameMinute.getReadBytes();
    }
    
    @Override
    public long getReadBytesPerSecond() {
        return frameSecond.getReadBytes();
    }
    
    @Override
    public long getWrittenBytesPerHour() {
        return frameHour.getWrittenBytes();
    }
    
    @Override
    public long getWrittenBytesPerMinute() {
        return frameMinute.getWrittenBytes();
    }
    
    @Override
    public long getWrittenBytesPerSecond() {
        return frameSecond.getWrittenBytes();
    }
    
    //=============== packets =================

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
    public void addDroppedOutPackets(long packets) {
        this.droppedOutPackets += packets;
    }

    @Override
    public void addWriteErrorPackets(long packets) {
        this.writeErrorPackets += packets;
    }

    
}
