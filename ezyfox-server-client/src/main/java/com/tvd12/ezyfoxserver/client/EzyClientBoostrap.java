/**
 * 
 */
package com.tvd12.ezyfoxserver.client;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.SocketException;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.constants.EzyConnectionError;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.controller.EzyConnectFailureController;
import com.tvd12.ezyfoxserver.client.handler.EzyClientHandler;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.netty.codec.EzyCombinedCodec;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

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
public class EzyClientBoostrap 
		extends EzyLoggable 
		implements EzyStartable, EzyDestroyable {

	protected int port;
    protected String host;
    protected EzyCodecCreator codecCreator;
    protected EzyClientContext clientContext;
    
    @Override
    public void start() throws Exception {
        EventLoopGroup group = newLoopGroup();
        try {
        	// new client bootstrap
            Bootstrap b = newBootstrap(group);
            
            getLogger().info("connecting to server ...");
            
            // connect to server
            ChannelFuture connectionFuture = b.connect();
            
            // wait and listen connection
            connectionFuture.syncUninterruptibly();
            
            // process connection successful
            if(connectionFuture.isSuccess())
            	processConnectSuccess(connectionFuture);
        }
        catch(ConnectException e) {
        	processConnectFailure(EzyConnectionError.CONNECTION_REFUSED);
        }
        catch(NoRouteToHostException e) {
        	processConnectFailure(EzyConnectionError.NO_ROUTE_TO_HOST);
        }
        catch(SocketException e) {
        	processConnectFailure(EzyConnectionError.NETWORK_UNREACHABLE);
        }
        catch(Exception e) {
        	throw e;
        }

        finally {
            group.shutdownGracefully().addListener((future) -> {
				getLogger().info("event loop group shutdown");
            }).sync();
        }
    }
    
    protected void processConnectFailure(EzyConstant error) {
        getLogger().info("connect to server failed");
        
        // notify to controller
        notifyConnectionFailure(error);
        
        // destroy this boostrap
        destroy();
    }
    
    protected void notifyConnectionFailure(EzyConstant error) {
        EzyConnectFailureController ctrl = 
        		getClient().getController(EzyClientCommand.CONNECT_FAILURE);
        ctrl.handle(error);
    }
    
    protected void processConnectSuccess(ChannelFuture connectFuture) throws Exception {
    	getLogger().info("connect to server successfully");
    	
    	// get close future channel
        ChannelFuture closeFuture = connectFuture.channel().closeFuture();
        
        // wait and listen close future state
        closeFuture.addListener((future) -> {
            getLogger().info("channel future close");
        })
        .sync();
    }
    
    protected Bootstrap newBootstrap(EventLoopGroup group) {
    	return new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(newChannelInitializer());
    }
    
    protected EventLoopGroup newLoopGroup() {
    	return new NioEventLoopGroup(3, EzyExecutors.newThreadFactory("clienteventloopgroup"));
    }
    
    protected ChannelInitializer<Channel> newChannelInitializer() {
    	return ClientChannelInitializer.builder()
    			.codecCreator(codecCreator)
    			.context(clientContext)
    			.build();
    }
    
    protected EzyClient getClient() {
    	return clientContext.getClient();
    }
    
    @Override
    public void destroy() {
    }
    
    public static Builder builder() {
    	return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyClientBoostrap> {
    	protected int port;
        protected String host;
        protected EzyCodecCreator codecCreator;
        protected EzyClientContext clientContext;
        
        public Builder port(int port) {
        	this.port = port;
        	return this;
        }
        
        public Builder host(String host) {
        	this.host = host;
            return this;
        }
        
        public Builder codecCreator(EzyCodecCreator codecCreator) {
        	this.codecCreator = codecCreator;
            return this;
        }
        
        public Builder clientContext(EzyClientContext clientContext) {
        	this.clientContext = clientContext;
            return this;
        }
        
        @Override
        public EzyClientBoostrap build() {
        	EzyClientBoostrap answer = new EzyClientBoostrap();
        	answer.host = host;
        	answer.port = port;
        	answer.codecCreator = codecCreator;
        	answer.clientContext = clientContext;
        	return answer;
        }
    }
    
}

@Builder
class ClientChannelInitializer extends ChannelInitializer<Channel> {

	private EzyClientContext context;
	private EzyCodecCreator codecCreator;
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		ChannelOutboundHandler encoder = (ChannelOutboundHandler) codecCreator.newEncoder();
		ChannelInboundHandlerAdapter decoder = (ChannelInboundHandlerAdapter) codecCreator.newDecoder(65536);
		pipeline.addLast("codec-1", new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast("handler", newDataHandler());
		pipeline.addLast("codec-2", new EzyCombinedCodec(decoder, encoder));
	}
	
	protected EzyClientHandler newDataHandler() {
		EzyClientHandler handler = new EzyClientHandler();
		handler.setContext(context);
		return handler;
	}
}
