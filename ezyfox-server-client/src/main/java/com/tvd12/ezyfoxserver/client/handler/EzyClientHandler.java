/**
 * 
 */
package com.tvd12.ezyfoxserver.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.client.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.client.serialize.iml.EzyRequestSerializerImpl;
import com.tvd12.ezyfoxserver.entity.EzyObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tavandung12
 *
 */
public class EzyClientHandler extends SimpleChannelInboundHandler<EzyObject> {

	private EzyRequestSerializer requestSerializer;
	
	{
		requestSerializer = EzyRequestSerializerImpl.builder().build();
	}
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	getLogger().info("active complete, be going to handshake");
    	ctx.writeAndFlush(requestSerializer.serializeToArray(
    			EzyHandShakeRequest.builder().token("12345678").build()
    	));
    	getLogger().info("has send handshake");
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EzyObject in) throws Exception {
        System.out.println("Client recived: " + in);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
    protected Logger getLogger() {
    	return LoggerFactory.getLogger(getClass());
    }
    
}
