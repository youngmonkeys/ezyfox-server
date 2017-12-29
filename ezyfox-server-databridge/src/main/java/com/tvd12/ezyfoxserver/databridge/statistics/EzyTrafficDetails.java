package com.tvd12.ezyfoxserver.databridge.statistics;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyTrafficDetails implements Serializable {
	private static final long serialVersionUID = 3537594634848099820L;

	private int maxUser;
	private int totalUser;
	
	private int socketMaxSession;
	private int socketTotalSession;
	private long socketTotalReadPackets;
	private long socketTotalWrittenPackets;
	private long socketTotalDroppedInPackets;
	private long socketTotalDroppedOutPackets;
	private long socketTransferredInputData;
	private long socketTransferredOutputData;
	private long socketCurrentInputDataTransferRate;
	private long socketCurrentOutputDataTransferRate;
	
	private int webSocketMaxSession;
	private int webSocketTotalSession;
	private long webSocketTotalReadPackets;
	private long webSocketTotalWrittenPackets;
	private long webSocketTotalDroppedInPackets;
	private long webSocketTotalDroppedOutPackets;
	private long webSocketTransferredInputData;
	private long webSocketTransferredOutputData;
	private long webSocketCurrentInputDataTransferRate;
	private long webSocketCurrentOutputDataTransferRate;
	
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
