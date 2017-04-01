package com.tvd12.ezyfoxserver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;

/**
 * Created by tavandung12 on 1/17/17.
 */
public class EzyChannelDataHandler extends ChannelInboundHandlerAdapter {

	@Setter
	protected EzyDataHandler dataHandler;
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		dataHandler.handlerAdded(ctx);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		dataHandler.handlerRemoved(ctx);
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	dataHandler.channelRead(ctx, msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	dataHandler.channelReadComplete(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	dataHandler.exceptionCaught(ctx, cause);
    }
    
}
