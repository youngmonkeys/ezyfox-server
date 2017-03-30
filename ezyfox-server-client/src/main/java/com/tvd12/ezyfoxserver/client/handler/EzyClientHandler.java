/**
 * 
 */
package com.tvd12.ezyfoxserver.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyClientContext;
import com.tvd12.ezyfoxserver.client.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constants.EzyClientConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.entity.impl.EzySimpleSession;
import com.tvd12.ezyfoxserver.exception.EzyResponseHandleException;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tavandung12
 *
 */
public class EzyClientHandler extends SimpleChannelInboundHandler<EzyArray> {

	private EzySession session;
	private EzyClientContext context;
	private EzyControllers controllers;
	private EzyRequestSerializer requestSerializer;

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	getLogger().info("active complete, be going to handshake");
    	createNewSession(ctx);
    	sendHandShakeRequest();
    	getLogger().info("has send handshake");
    }
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("handler removed");
		context.get(EzyShutdown.class).execute();
	}
	
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EzyArray msg) throws Exception {
    	getLogger().info("Client recived: " + msg);
		int cmdId = msg.get(0);
		EzyData data = msg.get(1);
		EzyCommand cmd = EzyCommand.valueOf(cmdId);
		handleResponse(cmd, data);
    }
    
    protected void sendHandShakeRequest() {
    	context.get(EzySendMessage.class)
    		.data(newHandShakeData())
    		.sender(session)
    		.execute();
    }
    
    protected EzyArray newHandShakeData() {
    	return requestSerializer.serializeToArray(newHandShakeRequest());
    }
    
    protected EzyHandShakeRequest newHandShakeRequest() {
    	return EzyHandShakeRequest.builder().build();
    }
    
    protected void createNewSession(ChannelHandlerContext ctx) {
    	this.session = newSession(ctx);
    }
    
    protected EzySession newSession(ChannelHandlerContext ctx) {
		EzySimpleSession session = new EzySimpleSession();
		session.setChannel(ctx.channel());
		return session;
	}
    
	protected void handleResponse(EzyCommand cmd, EzyData data) {
    	context.get(EzyRunWorker.class).run(() -> { 
    		tryHandleResponse(cmd, data);
    	});
    }
    
    @SuppressWarnings("unchecked")
	protected void tryHandleResponse(EzyCommand cmd, EzyData data) {
    	try {
    		controllers.getController(cmd).handle(context, getReceiver(), data);
    	}
    	catch(Exception e) {
    		throw new EzyResponseHandleException(newHandleRequestErrorMessage(cmd, data), e);
    	}
    }
    
    protected Object getReceiver() {
    	return getMe() == null ? session : getMe();
    }
    
    public void setContext(EzyClientContext ctx) {
    	this.context = ctx;
    	this.controllers = getClient().getControllers();
    	this.requestSerializer = ctx.get(EzyRequestSerializer.class);
    }
    
    protected EzyClient getClient() {
    	return context.getClient();
    }
    
    protected EzyUser getMe() {
    	return context.getProperty(EzyClientConstant.ME);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        getLogger().error("exception caught at session", cause);
    }
    
    protected String newHandleRequestErrorMessage(EzyConstant cmd, EzyData data) {
		return "error when handle request command: " + cmd + ", data: " + data;
	}
    
    protected Logger getLogger() {
    	return LoggerFactory.getLogger(getClass());
    }
    
}
