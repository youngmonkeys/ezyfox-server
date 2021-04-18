package com.tvd12.ezyfoxserver.nio.testing.websocket;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.codec.EzyStringToObjectDecoder;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.websocket.EzySimpleWsHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySimpleWsHandlerGroupTest extends BaseTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		EzySessionTicketsQueue socketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
		EzySessionTicketsQueue webSocketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
		EzySessionManager sessionManager = EzyNioSessionManagerImpl.builder()
				.maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
				.tokenGenerator(new EzySimpleSessionTokenGenerator())
				.build();
		EzyStatistics statistics = new EzySimpleStatistics();
		EzySimpleSettings settings = new EzySimpleSettings();
		EzySimpleStreamingSetting streaming = settings.getStreaming();
		streaming.setEnable(true);
		EzySimpleServer server = new EzySimpleServer();
		server.setSettings(settings);
		server.setSessionManager(sessionManager);
		EzySimpleServerContext serverContext = new EzySimpleServerContext();
		serverContext.setServer(server);
		serverContext.init();
		
		EzyChannel channel = mock(EzyChannel.class);
		when(channel.isConnected()).thenReturn(true);
		when(channel.getConnection()).thenReturn(SocketChannel.open());
		when(channel.getConnectionType()).thenReturn(EzyConnectionType.WEBSOCKET);
		EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
		ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
		ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
		EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
		EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();
		
		EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = EzyHandlerGroupBuilderFactoryImpl.builder()
				.statistics(statistics)
				.serverContext(serverContext)
				.statsThreadPool(statsThreadPool)
				.streamQueue(streamQueue)
				.codecFactory(new ExCodecFactory())
				.sessionTicketsRequestQueues(sessionTicketsRequestQueues)
				.socketSessionTicketsQueue(socketSessionTicketsQueue)
				.webSocketSessionTicketsQueue(webSocketSessionTicketsQueue)
				.build();
		
		EzySimpleWsHandlerGroup group = (EzySimpleWsHandlerGroup) handlerGroupBuilderFactory
				.newBuilder(channel, EzyConnectionType.WEBSOCKET)
				.build();
		
		group.fireBytesReceived("hello");
		group.fireBytesReceived(new byte[] {127, 2, 3, 4, 5, 6}, 0, 5);
		((EzyAbstractSession)session).setStreamingEnable(true);
		group.fireBytesReceived(new byte[] {127, 2, 3, 4, 5, 6}, 0, 5);
		EzySimplePacket packet = new EzySimplePacket();
		packet.setBinary(false);
		packet.setData("world".getBytes());
		packet.setTransportType(EzyTransportType.TCP);
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		group.firePacketSend(packet, writeBuffer);
		group.sendPacketNow(packet);
		group.fireChannelRead(EzyCommand.PING, EzyEntityArrays.newArray(EzyCommand.PING.getId(), EzyEntityFactory.EMPTY_ARRAY));
		group.fireStreamBytesReceived(new byte[] {0, 1, 2});
		EzyPacket droppedPacket = mock(EzyPacket.class);
		when(droppedPacket.getSize()).thenReturn(12);
		group.addDroppedPacket(droppedPacket);
		EzyPacket failedPacket = mock(EzyPacket.class);
		when(failedPacket.getData()).thenReturn(new byte[] {1, 2, 3});
		when(failedPacket.isBinary()).thenReturn(false);
		when(channel.write(any(ByteBuffer.class), anyBoolean())).thenThrow(new IllegalStateException("maintain"));
		group.firePacketSend(failedPacket, writeBuffer);
		
		MethodInvoker.create()
			.object(group)
			.method("executeHandleReceivedBytes")
			.param("hello")
			.invoke();
		
		MethodInvoker.create()
			.object(group)
			.method("executeHandleReceivedBytes")
			.param("hello".getBytes())
			.invoke();
		
		group.fireChannelInactive();
		Thread.sleep(2000);
		group.destroy();
		group.destroy();
		
		EzySocketStreamQueue streamQueue1 = mock(EzySocketStreamQueue.class);
		when(streamQueue1.add(any())).thenThrow(new IllegalStateException("queue full"));
		group = (EzySimpleWsHandlerGroup) handlerGroupBuilderFactory
				.newBuilder(channel, EzyConnectionType.WEBSOCKET)
				.session(session)
				.decoder(decoder)
				.serverContext(serverContext)
				.statsThreadPool(statsThreadPool)
				.streamQueue(streamQueue1)
				.sessionTicketsRequestQueues(sessionTicketsRequestQueues)
				.build();
		group.fireBytesReceived(new byte[] {127, 2, 3, 4, 5, 6}, 0, 5);
	}
	
	public static class ExCodecFactory implements EzyCodecFactory {
		@Override
		public Object newDecoder(EzyConstant type) {
			return new ExEzyByteToObjectDecoder();
		}

		@Override
		public Object newEncoder(EzyConstant type) {
			return null;
		}
	}
	
	public static class ExEzyByteToObjectDecoder implements EzyStringToObjectDecoder {

		@Override
		public Object decode(String bytes) throws Exception {
			return EzyEntityFactory.newArrayBuilder()
					.append(EzyCommand.PING.getId())
					.build();
		}

		@Override
		public Object decode(byte[] bytes) throws Exception {
			return EzyEntityFactory.newArrayBuilder()
					.append(EzyCommand.PING.getId())
					.build();
		}
		
	}
	
}
