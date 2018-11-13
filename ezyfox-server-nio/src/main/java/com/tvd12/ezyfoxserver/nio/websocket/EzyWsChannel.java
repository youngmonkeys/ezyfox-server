package com.tvd12.ezyfoxserver.nio.websocket;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithException;
import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;
import static com.tvd12.ezyfoxserver.nio.websocket.EzyWsCloseStatus.*;

import lombok.Getter;

@Getter
public class EzyWsChannel extends EzyLoggable implements EzyChannel {

	private final Session session;
	private final AtomicBoolean opened;
	private final SocketAddress serverAddress;
	private final SocketAddress clientAddress;
	
	public EzyWsChannel(Session session) {
		this.session = session;
		this.opened = new AtomicBoolean(true);
		this.serverAddress = session.getLocalAddress();
		this.clientAddress = session.getRemoteAddress();
	}
	
	@Override
	public int write(Object data) throws Exception {
		try {
			return write0(data);
		}
		catch(WebSocketException e) {
			logger.debug("write data: " + data + ", to: " + clientAddress + " error", e);
			return 0;
		}
	}
	
	private int write0(Object data) throws Exception {
		String bytes = (String)data;
		int bytesSize = bytes.length();
		RemoteEndpoint remote = session.getRemote();
		remote.sendString(bytes);
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
		return opened.get();
	}
	
	public void setClosed() {
		this.opened.set(false);
	}
	
	@Override
	public void disconnect() {
		processWithLogException(session::disconnect);
	}
	
	@Override
	public void close() {
		processWithException(() -> session.close(CLOSE_BY_SERVER));
	}
}
