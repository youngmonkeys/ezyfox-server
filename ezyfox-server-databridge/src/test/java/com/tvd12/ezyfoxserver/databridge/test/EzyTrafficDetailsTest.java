package com.tvd12.ezyfoxserver.databridge.test;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.databridge.statistics.EzyTrafficDetails;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;

public class EzyTrafficDetailsTest {

	@Test
	public void test() {
		EzyStatistics statistics = new EzySimpleStatistics();
		EzyTrafficDetails details = new EzyTrafficDetails(statistics);
		
		assert details.getMaxUser() == statistics.getUserStats().getMaxUsers();
		assert details.getTotalUser() == statistics.getUserStats().getTotalUsers();
		
		assert details.getSocketMaxSession() == statistics.getSocketStats().getSessionStats().getMaxSessions();
		assert details.getSocketTotalSession() == statistics.getSocketStats().getSessionStats().getTotalSessions();
		assert details.getSocketTotalReadPackets() == statistics.getSocketStats().getNetworkStats().getReadPackets();
		assert details.getSocketTotalWrittenPackets() == statistics.getSocketStats().getNetworkStats().getWrittenPackets();
		assert details.getSocketTotalDroppedInPackets() == statistics.getSocketStats().getNetworkStats().getDroppedInPackets();
		assert details.getSocketTransferredInputData() == statistics.getSocketStats().getNetworkStats().getReadBytes();
		assert details.getSocketTransferredOutputData() == statistics.getSocketStats().getNetworkStats().getWrittenBytes();
		assert details.getSocketCurrentInputDataTransferRate() == statistics.getSocketStats().getNetworkStats().getReadBytesPerSecond();
		assert details.getSocketCurrentOutputDataTransferRate() == statistics.getSocketStats().getNetworkStats().getWrittenBytesPerSecond();
		
		assert details.getWebSocketMaxSession() == statistics.getWebSocketStats().getSessionStats().getMaxSessions();
		assert details.getWebSocketTotalSession() == statistics.getWebSocketStats().getSessionStats().getTotalSessions();
		assert details.getWebSocketTotalReadPackets() == statistics.getWebSocketStats().getNetworkStats().getReadPackets();
		assert details.getWebSocketTotalWrittenPackets() == statistics.getWebSocketStats().getNetworkStats().getWrittenPackets();
		assert details.getWebSocketTotalDroppedInPackets() == statistics.getWebSocketStats().getNetworkStats().getDroppedInPackets();
		assert details.getWebSocketTotalDroppedOutPackets() == statistics.getWebSocketStats().getNetworkStats().getDroppedOutPackets();
		assert details.getWebSocketTransferredInputData() == statistics.getWebSocketStats().getNetworkStats().getReadBytes();
		assert details.getWebSocketTransferredOutputData() == statistics.getWebSocketStats().getNetworkStats().getDroppedOutPackets();
		assert details.getWebSocketCurrentInputDataTransferRate() == statistics.getWebSocketStats().getNetworkStats().getReadBytesPerSecond();
		assert details.getWebSocketCurrentOutputDataTransferRate() == statistics.getWebSocketStats().getNetworkStats().getWrittenBytesPerSecond();
		
		
		assert details.getMaxSession() == (details.getSocketMaxSession() + details.getWebSocketMaxSession());
		assert details.getTotalSession() == (details.getSocketTotalSession() + details.getWebSocketTotalSession());
		assert details.getTotalReadPackets() == (details.getSocketTotalReadPackets() + details.getWebSocketTotalReadPackets());
		assert details.getTotalWrittenPackets() == (details.getSocketTotalWrittenPackets() + details.getWebSocketTotalWrittenPackets());
		assert details.getTotalDroppedInPackets() == (details.getSocketTotalDroppedInPackets() + details.getWebSocketTotalDroppedInPackets());
		assert details.getTotalDroppedOutPackets() == (details.getSocketTotalDroppedOutPackets() + details.getWebSocketTotalDroppedOutPackets());
		assert details.getTransferredInputData() == (details.getSocketTransferredInputData() + details.getWebSocketTransferredInputData());
		assert details.getTransferredOutputData() == (details.getSocketTransferredOutputData() + details.getWebSocketTransferredOutputData());
		assert details.getCurrentInputDataTransferRate() == (details.getSocketCurrentInputDataTransferRate() + details.getWebSocketCurrentInputDataTransferRate());
		assert details.getCurrentOutputDataTransferRate() == (details.getSocketCurrentOutputDataTransferRate() + details.getWebSocketCurrentOutputDataTransferRate());
	}
	
}
