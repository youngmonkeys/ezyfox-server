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
	private long socketTransferredInputData;
	private long socketTransferredOutputData;
	private long socketCurrentInputDataTransferRate;
	private long socketCurrentOutputDataTransferRate;
	
	private int webSocketMaxSession;
	private int webSocketTotalSession;
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
	
	public long getTransferredInputData() {
		return socketTransferredInputData + webSocketTransferredInputData;
	}
	
	public long getTransferredOutputData() {
		return socketTransferredOutputData + webSocketTransferredOutputData;
	}
	
	public long getSocketCurrentInputDataTransferRate() {
		return socketCurrentInputDataTransferRate + webSocketCurrentInputDataTransferRate;
	}
	
	public long getSocketCurrentOutputDataTransferRate() {
		return socketCurrentOutputDataTransferRate + webSocketCurrentOutputDataTransferRate;
	}

}
