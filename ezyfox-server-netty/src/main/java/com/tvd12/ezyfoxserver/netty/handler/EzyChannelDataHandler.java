package com.tvd12.ezyfoxserver.netty.handler;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.handler.EzyBytesReceived;
import com.tvd12.ezyfoxserver.handler.EzyBytesSent;
import com.tvd12.ezyfoxserver.netty.socket.EzyNettyChannel;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;

/**
 * Created by tavandung12 on 1/17/17.
 */
public class EzyChannelDataHandler 
		extends ChannelInboundHandlerAdapter 
		implements EzyBytesReceived, EzyBytesSent, EzyHandlerGroupManagerAware {

	protected EzyHandlerGroup handlerGroup;
	@Setter
	protected EzyConnectionType connectionType;
	@Setter
	protected EzyHandlerGroupManager handlerGroupManager;

	@Override
	public void bytesSent(int bytes) {
		handlerGroup.fireBytesSent(bytes);
	}
	
	@Override
	public void bytesReceived(int bytes) {
		handlerGroup.fireBytesReceived(bytes);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		EzyChannel channel = new EzyNettyChannel(ctx.channel(), connectionType);
		handlerGroup = handlerGroupManager.newHandlerGroup(channel, connectionType);
		handlerGroup.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		handlerGroup.fireChannelInactive();
		handlerGroup = null;
		handlerGroupManager = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		handlerGroup.fireChannelRead((EzyArray) msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		handlerGroup.fireExceptionCaught(cause);
	}

}
