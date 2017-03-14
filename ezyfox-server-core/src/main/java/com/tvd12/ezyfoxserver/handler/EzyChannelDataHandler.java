package com.tvd12.ezyfoxserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;

/**
 * Created by tavandung12 on 1/17/17.
 */
public abstract class EzyChannelDataHandler extends ChannelInboundHandlerAdapter {
	
	@Getter
	protected Logger logger;
	@Getter
	protected EzySession session;
	@Getter
	protected EzySessionManager sessionManager;
	
	public EzyChannelDataHandler() {
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	protected abstract void sessionAdded(EzySession session);
	protected abstract void dataReceived(EzySession session, EzyArray msg);
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		handlerAdded(ctx, borrowSession(ctx));
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.info("session remove");
		returnSession(ctx);
	}
	
	protected EzySession borrowSession(ChannelHandlerContext ctx) {
		session = sessionManager.borrowSession(ctx.channel());
		return session;
	}
	
	protected void returnSession(ChannelHandlerContext ctx) {
		sessionManager.returnSession(session);
	}
	
	private void handlerAdded(ChannelHandlerContext ctx, EzySession session) {
		updateSessionAll(ctx, session);
		sessionAdded(session);
	}
	
	private void updateSessionAll(ChannelHandlerContext ctx, EzySession session) {
		session.setCreationTime(System.currentTimeMillis());
		session.setLastActivityTime(System.currentTimeMillis());
		session.setLastReadTime(System.currentTimeMillis());
		session.setLastWriteTime(System.currentTimeMillis());
		session.setProperty(ChannelHandlerContext.class, ctx);
	}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	dataReceived(session, (EzyArray)msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channel read complete");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	getLogger().error("exception caught at session " + session, cause);
    }
    
}
