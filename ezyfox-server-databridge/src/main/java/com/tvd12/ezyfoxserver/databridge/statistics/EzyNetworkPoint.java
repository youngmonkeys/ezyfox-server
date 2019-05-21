package com.tvd12.ezyfoxserver.databridge.statistics;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.statistics.EzyNetworkRoStats;
import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

import lombok.Getter;

@Getter
public class EzyNetworkPoint implements Serializable {
	private static final long serialVersionUID = -6537078366231599664L;
	
	private long inputBytes;
	private long outputBytes;
	
	public EzyNetworkPoint(EzyStatistics statistics) {
		EzySocketStatistics socketStats = statistics.getSocketStats();
		EzyWebSocketStatistics webSocketStats = statistics.getWebSocketStats();
		EzyNetworkRoStats socketNetworkStats = socketStats.getNetworkStats();
		EzyNetworkRoStats webSocketNetworkStats = webSocketStats.getNetworkStats();
		this.inputBytes = socketNetworkStats.getReadBytesPerSecond()
				+ webSocketNetworkStats.getReadBytesPerSecond();
		this.outputBytes = socketNetworkStats.getWrittenBytesPerSecond()
				+ webSocketNetworkStats.getWrittenBytesPerSecond();
	}
	
}
