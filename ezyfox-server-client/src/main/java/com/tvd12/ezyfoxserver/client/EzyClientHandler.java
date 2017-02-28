/**
 * 
 */
package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
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
		EzyObjectBuilder builder2 = EzyFactory
				.create(EzyObjectBuilder.class)
    			.append("hello", "world");
		EzyArrayBuilder arrayBuilder = EzyFactory
				.create(EzyArrayBuilder.class)
				.append(builder2);
		builder.append("array", arrayBuilder);
		for(int i = 0 ; i < 5 ; i++)
			builder.append("hello-" + i, "world-" + i);
		int count = 0;
		while(count ++ < 5) {
			Thread.sleep(1000);
			ctx.writeAndFlush(builder.build());
		}
    	System.out.println("active complete");
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
