package com.tvd12.ezyfoxserver.nio.handler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageHeader;
import com.tvd12.ezyfox.codec.EzyMessageReaders;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.io.EzyBytes;
import com.tvd12.ezyfox.io.EzyInts;
import com.tvd12.ezyfox.io.EzyLongs;
import com.tvd12.ezyfox.net.EzySocketAddresses;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelAware;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.ezyfoxserver.socket.EzyUdpClientAddressAware;
import com.tvd12.ezyfoxserver.socket.EzyUdpReceivedPacket;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManagerAware;

import lombok.Setter;

@SuppressWarnings("rawtypes")
public class EzySimpleNioUdpDataHandler	
		extends EzyLoggable 
		implements 
			EzyNioUdpDataHandler, 
			EzySessionManagerAware,
			EzyHandlerGroupManagerAware {

	@Setter
	protected EzyResponseApi responseApi;
	@Setter
	protected EzySessionManager sessionManager;
	@Setter
	protected EzyHandlerGroupManager handlerGroupManager;
	protected final ExecutorService executorService;
	
	public EzySimpleNioUdpDataHandler(int threadPoolSize) {
		this.executorService = 
				EzyExecutors.newFixedThreadPool(threadPoolSize, "udp-data-handler");
	}
	
	@Override
	public void fireUdpPacketReceived(EzyUdpReceivedPacket packet) throws Exception {
		this.executorService.execute(() -> handleReceivedUdpPacket(packet));
	}
	
	protected void handleReceivedUdpPacket(EzyUdpReceivedPacket packet) {
		try {
			EzyMessage message = EzyMessageReaders.bytesToMessage(packet.getBytes());
			if(message == null)
				return;
			InetSocketAddress udpAddress = packet.getAddress();
			EzyNioHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(udpAddress);
			if(handlerGroup != null) {
				EzyNioSession session = handlerGroup.getSession();
				SocketAddress tcpAddress = session.getClientAddress();
				if(isOneClient(tcpAddress, udpAddress))
					handlerGroup.fireMessageReceived(message);
			}
			else {
				EzyMessageHeader header = message.getHeader();
				if(header.isUdpHandshake())
					handleUdpHandshake(packet.getChannel(), udpAddress, message);
			}
		}
		catch (Exception e) {
			logger.warn("handle received udp package: {} error: {}", packet, e.getMessage());
		}
	}
	
	protected void handleUdpHandshake(
			DatagramChannel channel, 
			InetSocketAddress address, EzyMessage message) throws Exception {
		byte[] content = message.getContent();
		if(content.length < 11)
			return;
		byte[] sessionIdBytes = EzyBytes.copy(content, 0, 8);
		long sessionId = EzyLongs.bin2long(sessionIdBytes);
		byte[] tokenSizeBytes = EzyBytes.copy(content, 8, 2);
		int tokenSize = EzyInts.bin2int(tokenSizeBytes);
		if(tokenSize > 512)
			return;
		byte[] tokenBytes = EzyBytes.copy(content, 10, tokenSize);
		EzySession session = sessionManager.getSession(sessionId);
		if(session == null)
			return;
		int responseCode = 498;
		String token = new String(tokenBytes);
		String sessionToken = session.getToken();
		if(sessionToken.equals(token)) {
			responseCode = 200;
			SocketAddress oldUdpAddress = session.getUdpClientAddress();
			handlerGroupManager.unmapHandlerGroup(oldUdpAddress);
			handlerGroupManager.mapHandlerGroup(address, session);
			((EzyDatagramChannelAware)session).setDatagramChannel(channel);
			((EzyUdpClientAddressAware)session).setUdpClientAddress(address);
		}
		response(session, responseCode);
	}
	
	protected void response(EzySession recipient, int responseCode) throws Exception {
		EzyArray responseData = EzyEntityFactory.newArray();
		responseData.add(responseCode);
		EzyArray responseCommand = EzyEntityFactory.newArray();
		responseCommand.add(EzyCommand.UDP_HANDSHAKE.getId());
		responseCommand.add(responseData);
		EzySimplePackage response = new EzySimplePackage();
		response.addRecipient(recipient);
		response.setTransportType(EzyTransportType.UDP);
		response.setData(responseCommand);
		responseApi.response(response);
		logger.debug("response udp handshake to: {}, code: {}", recipient, responseCode);
	}
	
	protected boolean isOneClient(SocketAddress tcpAddress, SocketAddress udpAddress) {
		String tcpHost = EzySocketAddresses.getHost(tcpAddress);
		String udpHost = EzySocketAddresses.getHost(udpAddress);
		boolean answer = tcpHost.equals(udpHost);
		return answer;
	}
	
}
