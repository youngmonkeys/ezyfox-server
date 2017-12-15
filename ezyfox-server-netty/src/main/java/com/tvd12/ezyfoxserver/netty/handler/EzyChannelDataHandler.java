package com.tvd12.ezyfoxserver.netty.handler;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;

/**
 * Created by tavandung12 on 1/17/17.
 */
public class EzyChannelDataHandler 
		extends ChannelInboundHandlerAdapter 
		implements EzyBytesReceived, EzyBytesSent {

	@Setter
	protected EzyDataHandler dataHandler;

	@Setter
	protected EzyConnectionType connectionType;

	@Override
	public void bytesReceived(int count) {
		dataHandler.bytesReceived(count);
	}

	@Override
	public void bytesSent(int count) {
		dataHandler.bytesSent(count);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		dataHandler.channelActive(ctx, connectionType);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		dataHandler.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		dataHandler.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		dataHandler.exceptionCaught(ctx, cause);
	}

}
