package com.tvd12.ezyfoxserver.nio.testing.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.io.EzyByteBuffers;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioUdpDataHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzySimpleUdpReceivedPacket;
import com.tvd12.ezyfoxserver.socket.EzyUdpReceivedPacket;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzySimpleNioUdpDataHandlerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		String sessionToken = "12345678";
		long sessionId = 12345L;
		EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
		int tokenSize = sessionToken.length();
		int messageSize = 0;
		messageSize += 8; // sessionIdSize
		messageSize += 2; // tokenLengthSize
		messageSize += tokenSize; // messageSize
		ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
		byte header = 0;
		header |= 1 << 5;
		buffer.put(header);
		buffer.putShort((short)messageSize);
		buffer.putLong(sessionId);
		buffer.putShort((short)tokenSize);
		buffer.put(sessionToken.getBytes());
		buffer.flip();
		byte[] bytes = EzyByteBuffers.getBytes(buffer);
		EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
				DatagramChannel.open(),
				new InetSocketAddress(12345),
				bytes);
		EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
		EzySessionManager sessionManager = mock(EzySessionManager.class);
		EzySession session = spy(EzyAbstractSession.class);
		session.setToken(sessionToken);
		when(sessionManager.getSession(sessionId)).thenReturn(session);
		EzyResponseApi responseApi = mock(EzyResponseApi.class);
		EzyDatagramChannelPool channelPool = new EzyDatagramChannelPool(1);
		handler.setHandlerGroupManager(handlerGroupManager);
		handler.setSessionManager(sessionManager);
		handler.setResponseApi(responseApi);
		handler.setDatagramChannelPool(channelPool);
		handler.fireUdpPacketReceived(packet);
		Thread.sleep(200);
	}
	
	@Test
	public void testNotHandshakeCase() throws Exception {
		String sessionToken = "12345678";
		long sessionId = 12345L;
		EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
		int tokenSize = sessionToken.length();
		int messageSize = 0;
		messageSize += 8; // sessionIdSize
		messageSize += 2; // tokenLengthSize
		messageSize += tokenSize; // messageSize
		ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
		byte header = 0;
		buffer.put(header);
		buffer.putShort((short)messageSize);
		buffer.putLong(sessionId);
		buffer.putShort((short)tokenSize);
		buffer.put(sessionToken.getBytes());
		buffer.flip();
		byte[] bytes = EzyByteBuffers.getBytes(buffer);
		EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
				DatagramChannel.open(),
				new InetSocketAddress(12345),
				bytes);
		EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
		EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
		when(handlerGroupManager.getHandlerGroup(packet.getAddress())).thenReturn(handlerGroup);
		EzyNioSession session = new EzySimpleSession();
		session.setToken(sessionToken);
		EzyChannel channel =  mock(EzyChannel.class);
		when(channel.getClientAddress()).thenReturn(new InetSocketAddress(12345));
		session.setChannel(channel);
		when(handlerGroup.getSession()).thenReturn(session);
		handler.setHandlerGroupManager(handlerGroupManager);
		handler.fireUdpPacketReceived(packet);
		Thread.sleep(200);
	}
	
	@Test
	public void testExceptionCase() throws Exception {
		String sessionToken = "12345678";
		long sessionId = 12345L;
		EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
		int tokenSize = sessionToken.length();
		int messageSize = 0;
		messageSize += 8; // sessionIdSize
		messageSize += 2; // tokenLengthSize
		messageSize += tokenSize; // messageSize
		ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
		byte header = 0;
		buffer.put(header);
		buffer.putShort((short)messageSize);
		buffer.putLong(sessionId);
		buffer.putShort((short)tokenSize);
		buffer.put(sessionToken.getBytes());
		buffer.flip();
		byte[] bytes = EzyByteBuffers.getBytes(buffer);
		EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
				DatagramChannel.open(),
				new InetSocketAddress(12345),
				bytes);
		EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
		when(handlerGroupManager.getHandlerGroup(packet.getAddress())).thenThrow(new IllegalArgumentException("test"));
		handler.setHandlerGroupManager(handlerGroupManager);
		handler.fireUdpPacketReceived(packet);
		Thread.sleep(200);
	}
	
	@Test
	public void testInvalidMessageCase() throws Exception {
		EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
		byte[] bytes = new byte[] {1, 2, 3};
		EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
				DatagramChannel.open(),
				new InetSocketAddress(12345),
				bytes);
		handler.fireUdpPacketReceived(packet);
		Thread.sleep(200);
	}
}
