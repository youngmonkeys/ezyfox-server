package com.tvd12.ezyfoxserver.databridge.statistics;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyUserStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

import lombok.Getter;

@Getter
public class EzyTrafficDetails implements Serializable {
	protected static final long serialVersionUID = 3537594634848099820L;

	protected int maxUser;
	protected int totalUser;
	
	protected int socketMaxSession;
	protected int socketTotalSession;
	protected long socketTotalReadPackets;
	protected long socketTotalWrittenPackets;
	protected long socketTotalDroppedInPackets;
	protected long socketTotalDroppedOutPackets;
	protected long socketTransferredInputData;
	protected long socketTransferredOutputData;
	protected long socketCurrentInputDataTransferRate;
	protected long socketCurrentOutputDataTransferRate;
	
	protected int webSocketMaxSession;
	protected int webSocketTotalSession;
	protected long webSocketTotalReadPackets;
	protected long webSocketTotalWrittenPackets;
	protected long webSocketTotalDroppedInPackets;
	protected long webSocketTotalDroppedOutPackets;
	protected long webSocketTransferredInputData;
	protected long webSocketTransferredOutputData;
	protected long webSocketCurrentInputDataTransferRate;
	protected long webSocketCurrentOutputDataTransferRate;
	
	public EzyTrafficDetails(EzyStatistics statistics) {
		EzyUserStatistics userStatistics = statistics.getUserStats();
		EzySocketStatistics socketStatistics = statistics.getSocketStats();
		EzyWebSocketStatistics webSocketStatistics = statistics.getWebSocketStats();
		
		this.maxUser = userStatistics.getMaxUsers();
		this.totalUser = userStatistics.getTotalUsers();
		
		this.socketMaxSession = socketStatistics.getSessionStats().getMaxSessions();
		this.socketTotalSession = socketStatistics.getSessionStats().getTotalSessions();
		this.socketTotalReadPackets = socketStatistics.getNetworkStats().getReadPackets();
		this.socketTotalWrittenPackets = socketStatistics.getNetworkStats().getWrittenPackets();
		this.socketTotalDroppedInPackets = socketStatistics.getNetworkStats().getDroppedInPackets();
		this.socketTotalDroppedOutPackets = socketStatistics.getNetworkStats().getDroppedOutPackets();
		this.socketTransferredInputData = socketStatistics.getNetworkStats().getReadBytes();
		this.socketTransferredOutputData = socketStatistics.getNetworkStats().getWrittenBytes();
		this.socketCurrentInputDataTransferRate = socketStatistics.getNetworkStats().getReadBytesPerSecond();
		this.socketCurrentOutputDataTransferRate = socketStatistics.getNetworkStats().getWrittenBytesPerSecond();
		
		this.webSocketMaxSession= webSocketStatistics.getSessionStats().getMaxSessions();
		this.webSocketTotalSession= webSocketStatistics.getSessionStats().getTotalSessions();
		this.webSocketTotalReadPackets= webSocketStatistics.getNetworkStats().getReadPackets();
		this.webSocketTotalWrittenPackets= webSocketStatistics.getNetworkStats().getWrittenPackets();
		this.webSocketTotalDroppedInPackets= webSocketStatistics.getNetworkStats().getDroppedInPackets();
		this.webSocketTotalDroppedOutPackets= webSocketStatistics.getNetworkStats().getDroppedOutPackets();
		this.webSocketTransferredInputData= webSocketStatistics.getNetworkStats().getReadBytes();
		this.webSocketTransferredOutputData= webSocketStatistics.getNetworkStats().getWrittenBytes();
		this.webSocketCurrentInputDataTransferRate= webSocketStatistics.getNetworkStats().getReadBytesPerSecond();
		this.webSocketCurrentOutputDataTransferRate= webSocketStatistics.getNetworkStats().getWrittenBytesPerSecond();
	}
	
	public int getMaxSession() {
		return socketMaxSession + webSocketMaxSession;
	}
	
	public int getTotalSession() {
		return socketTotalSession + webSocketTotalSession;
	}
	
	public long getTotalReadPackets() {
		return socketTotalReadPackets + webSocketTotalReadPackets;
	}
	
	public long getTotalWrittenPackets() {
		return socketTotalWrittenPackets + webSocketTotalWrittenPackets;
	}
	
	public long getTotalDroppedInPackets() {
		return socketTotalDroppedInPackets + webSocketTotalDroppedInPackets;
	}
	
	public long getTotalDroppedOutPackets() {
		return socketTotalDroppedOutPackets + webSocketTotalDroppedOutPackets;
	}
	
	public long getTransferredInputData() {
		return socketTransferredInputData + webSocketTransferredInputData;
	}
	
	public long getTransferredOutputData() {
		return socketTransferredOutputData + webSocketTransferredOutputData;
	}
	
	public long getCurrentInputDataTransferRate() {
		return socketCurrentInputDataTransferRate + webSocketCurrentInputDataTransferRate;
	}
	
	public long getCurrentOutputDataTransferRate() {
		return socketCurrentOutputDataTransferRate + webSocketCurrentOutputDataTransferRate;
	}
	

}
