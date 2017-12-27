package com.tvd12.ezyfoxserver.nio.handler;

import static com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason.MAX_REQUEST_SIZE;

import java.util.Map;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketChannelDelegate;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;

import lombok.Setter;

public class EzySimpleNioDataHandler
		extends EzySimpleDataHandler<EzyNioSession>
		implements EzyNioDataHandler {	
	
	protected final EzyChannel channel;

	@Setter
	protected EzySocketChannelDelegate channelDelegate;
	
	public EzySimpleNioDataHandler(EzyChannel channel) {
		this.channel = channel;
	}

	@Override
	public EzyNioSession channelActive() throws Exception {
		getLogger().debug("channel actived, add session");
		provideSession();
		sessionActive();
		return session;
	}
	
	@Override
	public void channelRead(EzyCommand cmd, EzyArray msg)  throws Exception {
    		dataReceived(cmd, msg);
    }
	
	@Override
	public void onSessionRemoved(EzyConstant reason) {
		this.channelDelegate.onChannelInactivated(channel);
		super.onSessionRemoved(reason);
	}
    
	private void provideSession() {
		provideSession(this::newSession);
	}
	
	private EzyNioSession newSession() {
		EzyNioSessionManager sessionManager = getNioSessionManager();
		return sessionManager.provideSession(channel);
	}
	
	@Override
	protected void addExceptionHandlers(Map<Class<?>, EzyExceptionHandler> handlers) {
		super.addExceptionHandlers(handlers);
		handlers.put(EzyMaxRequestSizeException.class, (thread, throwable) -> {
			if(sessionManager != null) 
	            sessionManager.removeSession(session, MAX_REQUEST_SIZE);
		});
	}
	
	private EzyNioSessionManager getNioSessionManager() {
		return (EzyNioSessionManager) sessionManager;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		this.channelDelegate = null;
	}
    
}
