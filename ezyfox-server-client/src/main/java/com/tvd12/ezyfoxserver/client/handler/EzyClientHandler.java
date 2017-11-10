/**
 * 
 */
package com.tvd12.ezyfoxserver.client.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.cmd.EzyPingSchedule;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.constants.EzyClientConstant;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.controller.EzyClientController;
import com.tvd12.ezyfoxserver.client.entity.EzyClientSession;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.client.entity.EzySimpleClientSession;
import com.tvd12.ezyfoxserver.client.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.exception.EzyResponseHandleException;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.netty.handler.EzyBytesReceived;
import com.tvd12.ezyfoxserver.netty.handler.EzyBytesSent;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tavandung12
 *
 */
public class EzyClientHandler 
		extends SimpleChannelInboundHandler<EzyArray>
		implements EzyBytesReceived, EzyBytesSent {

	private EzyClient client;
	private EzyClientSession session;
	private EzyClientContext context;
	protected Set<EzyConstant> unloggableCommands;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void bytesReceived(int count) {
		session.addReadBytes(count);
	}
	
	@Override
	public void bytesSent(int count) {
		session.addWrittenBytes(count);
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("channel active");
    	createNewSession(ctx);
    	updateContextWithNewSession();
    	notifyConnectSuccess(ctx);
    	startPingSchedule();
    }
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("channel inactive");
		stopPingSchedule();
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("channel register");
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("channel unregister");
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("handler added");
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("handler removed");
	}
	
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EzyArray msg) throws Exception {
		int cmdId = msg.get(0);
		EzyData data = msg.get(1);
		EzyClientCommand cmd = EzyClientCommand.valueOf(cmdId);
		debugLogReceivedData(cmd, data);
		handleResponse(cmd, data);
    }
    
    protected void debugLogReceivedData(EzyClientCommand cmd, EzyData data) {
    	if(!unloggableCommands.contains(cmd))
    		getLogger().info("client fire command: {} with data: {}", cmd, data);
    }
    
    protected void startPingSchedule() {
    	context.get(EzyPingSchedule.class).start();
    }
    
    protected void stopPingSchedule() {
    	context.get(EzyPingSchedule.class).stop();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void notifyConnectSuccess(ChannelHandlerContext ctx) {
    	EzyClientController ctrl =
    			client.getController(EzyClientCommand.CONNECT_SUCCESS);
    	ctrl.handle(context, session, newArrayBuilder().build());
    }
    
    protected EzyHandShakeRequest newHandShakeRequest() {
    	return EzyHandShakeRequest.builder().build();
    }
    
    protected void createNewSession(ChannelHandlerContext ctx) {
    	this.session = newSession(ctx);
    }
    
    protected void updateContextWithNewSession() {
    	this.context.getMe().setSession(session);
    }
    
    protected EzyClientSession newSession(ChannelHandlerContext ctx) {
    	EzySimpleClientSession session = new EzySimpleClientSession();
		session.setChannel(ctx.channel());
		return session;
	}
    
	protected void handleResponse(EzyClientCommand cmd, EzyData data) {
    	context.get(EzyRunWorker.class).run(() -> { 
    		tryHandleResponse(cmd, data);
    	});
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void tryHandleResponse(EzyClientCommand cmd, EzyData data) {
    	try {
    		EzyClientController ctr = client.getController(cmd);
    		ctr.handle(context, getReceiver(cmd), (EzyArray) data);
    	}
    	catch(Exception e) {
    		throw new EzyResponseHandleException(newHandleRequestErrorMessage(cmd, data), e);
    	}
    }
    
    protected Object getReceiver(EzyClientCommand cmd) {
    	return EzyReceiverDeterminer.builder()
    			.userSupplier(() -> getMe())
    			.sessionSupplier(() -> session)
    			.build()
    			.determine(cmd);
    }
    
    public void setContext(EzyClientContext ctx) {
    	this.context = ctx;
    	this.client = context.getClient();
    	this.unloggableCommands = client.getUnloggableCommands();
    }
    
    protected EzyClient getClient() {
    	return context.getClient();
    }
    
    protected EzyClientUser getMe() {
    	return context.getProperty(EzyClientConstant.ME);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        getLogger().error("exception caught at session", cause);
    }
    
    protected String newHandleRequestErrorMessage(EzyConstant cmd, EzyData data) {
		return "error when handle request command: " + cmd + ", data: " + data;
	}
    
    protected EzyArrayBuilder newArrayBuilder() {
    	return EzyEntityFactory.create(EzyArrayBuilder.class);
    }
    
    protected Logger getLogger() {
    	return logger;
    }
    
}
