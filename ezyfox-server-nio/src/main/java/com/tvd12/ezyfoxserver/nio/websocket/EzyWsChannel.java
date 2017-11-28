package com.tvd12.ezyfoxserver.nio.websocket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithException;
import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.net.SocketAddress;

import org.eclipse.jetty.websocket.api.Session;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import lombok.Getter;

@Getter
public class EzyWsChannel implements EzyChannel {

	private final Session session;
	private final SocketAddress serverAddress;
	private final SocketAddress clientAddress;
	
	public EzyWsChannel(Session session) {
		this.session = session;
		this.serverAddress = session.getLocalAddress();
		this.clientAddress = session.getRemoteAddress();
	}
	
	@Override
	public int write(Object data) throws Exception {
		String bytes = (String)data;
		int bytesSize = bytes.length();
		session.getRemote().sendString(bytes);
		return bytesSize;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Session getConnection() {
		return session;
	}
	
	@Override
	public EzyConnectionType getConnectionType() {
		return EzyConnectionType.WEBSOCKET;
	}
	
	@Override
	public boolean isConnected() {
		return session.isOpen();
	}
	
	@Override
	public void disconnect() {
		processWithLogException(session::disconnect);
	}
	
	@Override
	public void close() {
		processWithException(session::close);
	}
}
