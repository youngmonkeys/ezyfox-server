/**
 * 
 */
package com.tvd12.ezyfoxserver.client;

import java.net.InetSocketAddress;

import com.tvd12.ezyfoxserver.client.handler.EzyClientHandler;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.client.serialize.iml.EzyRequestSerializerImpl;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.codec.EzyCombinedCodec;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Builder;

/**
 * @author tavandung12
 *
 */
@Builder
public class EzyClientBoostrap 
		extends EzyLoggable 
		implements EzyStartable, EzyDestroyable {

	protected int port;
    protected String host;
    protected EzyClient client;
    protected EzyCodecCreator codecCreator;
    
    @Override
    public void start() throws Exception {
        EventLoopGroup group = newLoopGroup();
        try {
            Bootstrap b = newBootstrap(group);
            getLogger().info("client connecting...");
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().addListener((future) ->
            		getLogger().info("channel future close")
			).sync();
        }
        finally {
            group.shutdownGracefully().addListener((future) ->
				getLogger().info("event loop group shutdown")
            ).sync();
        }
    }
    
    @Override
    public void destroy() {
    }
    
    protected Bootstrap newBootstrap(EventLoopGroup group) {
    	return new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(newChannelInitializer());
    }
    
    protected EventLoopGroup newLoopGroup() {
    	return new NioEventLoopGroup(39, EzyExecutors.newThreadFactory("clienteventloopgroup"));
    }
    
    protected ChannelInitializer<Channel> newChannelInitializer() {
    	return ClientChannelInitializer.builder()
    			.codecCreator(codecCreator)
    			.context(newContext())
    			.build();
    }
    
    protected EzyClientContext newContext() {
    	EzySimpleClientContext ctx = new EzySimpleClientContext();
    	ctx.setClient(client);
    	ctx.setProperty(EzyRequestSerializer.class,	newRequestSerializer());
    	ctx.setWorkerExecutor(EzyExecutors.newFixedThreadPool(39, "client-worker"));
    	return ctx;
    }
    
    protected EzyRequestSerializer newRequestSerializer() {
    	return EzyRequestSerializerImpl.builder().build();
    }
    
}

@Builder
class ClientChannelInitializer extends ChannelInitializer<Channel> {

	private EzyClientContext context;
	private EzyCodecCreator codecCreator;
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		ChannelOutboundHandler encoder = codecCreator.newEncoder();
		ChannelInboundHandlerAdapter decoder = codecCreator.newDecoder();
		pipeline.addLast(new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast(newDataHandler());
		pipeline.addLast(new EzyCombinedCodec(decoder, encoder));
	}
	
	protected EzyClientHandler newDataHandler() {
		EzyClientHandler handler = new EzyClientHandler();
		handler.setContext(context);
		return handler;
	}
}
