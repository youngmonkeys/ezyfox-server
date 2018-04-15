package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.function.Function;

import com.tvd12.ezyfoxserver.databridge.statistics.EzyNetworkPoint;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkRoStats;

public class EzyNetworkStatsController extends EzyStatisticsController {

	public long getTotalReadBytes() {
		return sumStatistics(stats -> stats.getReadBytes());
	}
	
	public long getReadBytesPerHour() {
		return sumStatistics(stats -> stats.getReadBytesPerHour());
	}
	
	public long getReadBytesPerMinute() {
		return sumStatistics(stats -> stats.getReadBytesPerMinute());
	}
	
	public long getReadBytesPerSecond() {
		return sumStatistics(stats -> stats.getReadBytesPerSecond());
	}
	
	public long getWrittenBytesPerHour() {
		return sumStatistics(stats -> stats.getWrittenBytesPerHour());
	}
	
	public long getWrittenBytesPerMinute() {
		return sumStatistics(stats -> stats.getWrittenBytesPerMinute());
	}
	
	public long getWrittenBytesPerSecond() {
		return sumStatistics(stats -> stats.getWrittenBytesPerSecond());
	}
	
	public EzyNetworkPoint getReadWrittenBytesPerHour() {
		return getNetworkPoint(
				stats -> stats.getReadBytesPerHour(),
				stats -> stats.getWrittenBytesPerHour());
	}
	
	public EzyNetworkPoint getReadWrittenBytesPerMinute() {
		return getNetworkPoint(
				stats -> stats.getReadBytesPerMinute(),
				stats -> stats.getWrittenBytesPerMinute());
	}
	
	public EzyNetworkPoint getReadWrittenBytesPerSecond() {
		return getNetworkPoint(
				stats -> stats.getReadBytesPerSecond(),
				stats -> stats.getWrittenBytesPerSecond());
	}
	
	protected long sumStatistics(Function<EzyNetworkRoStats, Long> function) {
		EzyNetworkRoStats socketNetworkStats = getSocketNetworkStats();
		EzyNetworkRoStats webSocketNetworkStats = getWebSocketNetworkStats();
		long sum = function.apply(socketNetworkStats) + function.apply(webSocketNetworkStats);
		return sum;
	}
	
	protected EzyNetworkPoint getNetworkPoint(
			Function<EzyNetworkRoStats, Long> readFunction,
			Function<EzyNetworkRoStats, Long> writtenFunction) {
		EzyNetworkRoStats socketNetworkStats = getSocketNetworkStats();
		EzyNetworkRoStats webSocketNetworkStats = getWebSocketNetworkStats();
		long sumRead = readFunction.apply(socketNetworkStats) 
				+ readFunction.apply(webSocketNetworkStats);
		long sumWritten = writtenFunction.apply(webSocketNetworkStats) 
				+ writtenFunction.apply(webSocketNetworkStats);
		EzyNetworkPoint point = new EzyNetworkPoint();
		point.setInputBytes(sumRead);
		point.setOutputBytes(sumWritten);
		return point;
	}
	
	protected EzyNetworkRoStats getSocketNetworkStats() {
		return getSocketStatistics().getNetworkStats();
	}
	
	protected EzyNetworkRoStats getWebSocketNetworkStats() {
		return getWebSocketStatistics().getNetworkStats();
	}
	
}
