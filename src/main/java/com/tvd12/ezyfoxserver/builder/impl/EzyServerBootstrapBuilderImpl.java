package com.tvd12.ezyfoxserver.builder.impl;

import java.net.InetSocketAddress;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.handler.EzyDataHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EzyServerBootstrapBuilderImpl implements EzyServerBootstrapBuilder {

	private int port;
	private EventLoopGroup childGroup;
	private EventLoopGroup parentGroup;
	private String combinedCodecHandler;
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#port(int)
	 */
	@Override
	public EzyServerBootstrapBuilder port(final int port) {
		this.port = port;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#childGroup(io.netty.channel.EventLoopGroup)
	 */
	@Override
	public EzyServerBootstrapBuilder childGroup(EventLoopGroup childGroup) {
		this.childGroup = childGroup;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#parentGroup(io.netty.channel.EventLoopGroup)
	 */
	@Override
	public EzyServerBootstrapBuilder parentGroup(EventLoopGroup parentGroup) {
		this.parentGroup = parentGroup;
		return this;
	}
	
	@Override
	public EzyServerBootstrapBuilder combinedCodecHandler(String className) {
		this.combinedCodecHandler = className;
		return this;
	}
	
	public EzyServerBootstrap build() {
		EzyServerBootstrap answer = new EzyServerBootstrap();
		answer.setChildGroup(childGroup);
		answer.setParentGroup(parentGroup);
		answer.setServerBootstrap(createServerBootstrap());
		return answer;
	}
	
	protected ServerBootstrap createServerBootstrap() {
		return newServerBootstrap()
				.group(parentGroup, childGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(newChannelInitializer());
	}
	
	private ChannelInitializer<Channel> newChannelInitializer() {
		return EzyChannelInitializer.builder()
				.combinedCodecHandler(combinedCodecHandler).build();
	}
	
	private ServerBootstrap newServerBootstrap() {
		return new ServerBootstrap() {
			@Override
			public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
				return childGroup != null 
						? super.group(parentGroup, childGroup) 
						: super.group(parentGroup);
			}
		};
	}
}

class EzyChannelInitializer extends ChannelInitializer<Channel> {
	
	@SuppressWarnings("rawtypes")
	protected CombinedChannelDuplexHandler combinedCodecHandler;
	
	protected EzyChannelInitializer(Builder builder) {
		this.combinedCodecHandler = builder.combinedCodecHandler;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(combinedCodecHandler);
		pipeline.addLast(new EzyDataHandler());
		pipeline.addLast(combinedCodecHandler);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		@SuppressWarnings("rawtypes")
		protected CombinedChannelDuplexHandler combinedCodecHandler;
		
		public Builder combinedCodecHandler(final String className) {
			this.combinedCodecHandler = newCombinedCodecHandler(className);
			return this;
		}
		
		public EzyChannelInitializer build() {
			return new EzyChannelInitializer(this);
		}
		
		@SuppressWarnings("rawtypes")
		private CombinedChannelDuplexHandler newCombinedCodecHandler(final String className) {
			try {
				return (CombinedChannelDuplexHandler) Class.forName(className).newInstance();
			}
			catch(Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}


