package com.tvd12.ezyfoxserver.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Listing 12.2 of <i>Netty in Action</i>
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EzyWsFrameHandler extends ChannelInboundHandlerAdapter {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) 
			throws Exception {
		channelRead(ctx, (TextWebSocketFrame)msg);
	}
	
    protected void channelRead(ChannelHandlerContext ctx, TextWebSocketFrame msg)
    		throws Exception {
    	ctx.fireChannelRead(msg.content());
    }
    
    protected Logger getLogger() {
    	return logger;
    }
    
}
