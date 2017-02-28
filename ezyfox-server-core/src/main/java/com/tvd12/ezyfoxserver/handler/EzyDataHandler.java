package com.tvd12.ezyfoxserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tavandung12 on 1/17/17.
 */
public class EzyDataHandler extends ChannelInboundHandlerAdapter {
	
	@Getter
	private Logger logger;	
	@Setter
	private EzySessionManager sessionManager;
	
	public EzyDataHandler() {
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		EzySession session = sessionManager.borrowSession(channel);
		session.setCreationTime(System.currentTimeMillis());
		session.setLastActivityTime(System.currentTimeMillis());
		session.setLastReadTime(System.currentTimeMillis());
		session.setLastWriteTime(System.currentTimeMillis());
		session.setClientAddress(channel.remoteAddress());
		session.setServerAddress(channel.localAddress());
		logger.info("add session: {}", session);
	}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	EzySession session = sessionManager.getSession(ctx.channel());
    	logger.info("server received: {}", msg);
    	logger.info("from session: {}", session);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channel read complete");
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
