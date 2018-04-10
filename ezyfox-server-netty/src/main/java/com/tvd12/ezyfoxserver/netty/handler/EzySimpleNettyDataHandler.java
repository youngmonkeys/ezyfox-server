package com.tvd12.ezyfoxserver.netty.handler;

import static com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason.MAX_REQUEST_SIZE;

import java.util.Map;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyNettySessionManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketChannelDelegate;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

public class EzySimpleNettyDataHandler 
		extends EzySimpleDataHandler<EzyNettySession>
		implements EzyNettyDataHandler {
	
	@Setter
	protected EzyChannel channel;
	@Setter
	protected EzySocketChannelDelegate channelDelegate;
	
	@Override
	public EzyNettySession channelActive() throws Exception {
		getLogger().debug("channel actived, add session");
		provideSession();
		sessionActive();
		return session;
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("channel {} inactive", ctx.channel().remoteAddress());
		channelInactive();
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
	
	private EzyNettySession newSession() {
		EzyNettySessionManager sessionManager = getNettySessionManager();
		return sessionManager.provideSession(channel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void addExceptionHandlers(Map<Class<?>, EzyExceptionHandler> handlers) {
		super.addExceptionHandlers(handlers);
		handlers.put(EzyMaxRequestSizeException.class, (thread, throwable) -> {
			if(sessionManager != null) 
	            sessionManager.removeSession(session, MAX_REQUEST_SIZE);
		});
	}

	private EzyNettySessionManager getNettySessionManager() {
		return (EzyNettySessionManager) sessionManager;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		this.channel = null;
		this.channelDelegate = null;
	}
	
}
