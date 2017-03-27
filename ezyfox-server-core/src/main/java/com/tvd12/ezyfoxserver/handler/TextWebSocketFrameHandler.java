package com.tvd12.ezyfoxserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;

/**
 * Listing 12.2 of <i>Netty in Action</i>
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class TextWebSocketFrameHandler
    extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
        throws Exception {
    	getLogger().info("user Event Triggered {}", evt);
        if (evt instanceof HandshakeComplete) {
            ctx.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

	@Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
        throws Exception {
    	TextWebSocketFrame res = new TextWebSocketFrame("hello world");
    	getLogger().info("channel read0 {}", msg.text());
    	ctx.writeAndFlush(res);
    }
    
    protected Logger getLogger() {
    	return LoggerFactory.getLogger(getClass());
    }
}
