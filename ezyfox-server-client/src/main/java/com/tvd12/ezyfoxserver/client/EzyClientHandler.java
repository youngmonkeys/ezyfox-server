/**
 * 
 */
package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tavandung12
 *
 */
public class EzyClientHandler extends SimpleChannelInboundHandler<EzyObject> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("channel active");
		EzyObjectBuilder builder = EzyFactory
				.create(EzyObjectBuilder.class)
    			.append("hello", "world");
		for(int i = 0 ; i < 200 ; i++)
			builder.append("hello-" + i, "world-" + i);
		int count = 0;
		while(count ++ < 100) {
			Thread.sleep(1000);
			ctx.writeAndFlush(builder.build());
		}
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
    
}
