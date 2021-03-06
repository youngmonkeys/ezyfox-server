package com.tvd12.ezyfoxserver.nio.testing.handler;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.testing.handler.EzySimpleNioHandlerGroupTest.ExEzyByteToObjectDecoder;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPoolAware;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzyUdpClientAddressAware;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyAbstractHandlerGroupTest extends BaseTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		EzyStatistics statistics = new EzySimpleStatistics();
		EzySimpleSettings settings = new EzySimpleSettings();
		EzySimpleStreamingSetting streaming = settings.getStreaming();
		streaming.setEnable(true);
		EzySimpleServer server = new EzySimpleServer();
		server.setSettings(settings);
		EzySimpleServerContext serverContext = new EzySimpleServerContext();
		serverContext.setServer(server);
		serverContext.init();
		
		EzySessionManager sessionManager = EzyNioSessionManagerImpl.builder()
				.maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
				.tokenGenerator(new EzySimpleSessionTokenGenerator())
				.build();
		EzyChannel channel = mock(EzyChannel.class);
		when(channel.isConnected()).thenReturn(true);
		when(channel.getConnection()).thenReturn(SocketChannel.open());
		when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
		EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
		ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
		ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
		EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();
		
		when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);
		
		ExHandlerGroup group = (ExHandlerGroup) new ExHandlerGroup.Builder()
				.session(session)
				.decoder(decoder)
				.sessionCount(new AtomicInteger())
				.networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
				.sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
				.serverContext(serverContext)
				.statsThreadPool(statsThreadPool)
				.sessionTicketsRequestQueues(sessionTicketsRequestQueues)
				.build();
		
		EzyChannel channelX = mock(EzyChannel.class);
		MethodInvoker.create()
			.object(group)
			.method("canWriteBytes")
			.param(EzyChannel.class, null)
			.invoke();
		MethodInvoker.create()
			.object(group)
			.method("canWriteBytes")
			.param(EzyChannel.class, channelX)
			.invoke();
		
		sessionTicketsRequestQueues = mock(EzySessionTicketsRequestQueues.class);
		when(sessionTicketsRequestQueues.addRequest(any())).thenReturn(false);
		group = (ExHandlerGroup) new ExHandlerGroup.Builder()
				.session(session)
				.decoder(decoder)
				.sessionCount(new AtomicInteger())
				.networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
				.sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
				.serverContext(serverContext)
				.statsThreadPool(statsThreadPool)
				.sessionTicketsRequestQueues(sessionTicketsRequestQueues)
				.build();
		
		MethodInvoker.create()
			.object(group)
			.method("handleReceivedData")
			.param(Object.class, EzyEntityFactory.newArrayBuilder()
					.append(EzyCommand.PING.getId())
					.append(EzyEntityFactory.EMPTY_OBJECT)
					.build())
			.param(int.class, 100)
			.invoke();
		
		((EzyDatagramChannelPoolAware)session).setDatagramChannelPool(new EzyDatagramChannelPool(1));
		((EzyUdpClientAddressAware)session).setUdpClientAddress(new InetSocketAddress("127.0.0.1", 12348));
		
		EzyPacket packet = mock(EzyPacket.class);
		when(packet.getData()).thenReturn(new byte[] {1, 2, 3});
		ByteBuffer buffer = ByteBuffer.allocate(100);
		MethodInvoker.create()
			.object(group)
			.method("sendPacketToClient0")
			.param(EzyPacket.class, packet)
			.param(Object.class, buffer)
			.invoke();
		
		MethodInvoker.create()
			.object(group)
			.method("sendPacketToClient0")
			.param(EzyPacket.class, packet)
			.param(Object.class, new Object())
			.invoke();
	}
	
	@SuppressWarnings("rawtypes")
	public static class ExHandlerGroup 
			extends EzyAbstractHandlerGroup
			implements EzyHandlerGroup {
		
		@Override
		protected EzyDestroyable newDecoder(Object decoder) {
			return null;
		}
		
		protected ExHandlerGroup(Builder builder) {
			super(builder);
		}
		
		public static class Builder extends EzyAbstractHandlerGroup.Builder {

			@Override
			public ExHandlerGroup build() {
				return new ExHandlerGroup(this);
			}
			
		}

	}
	
}
