package com.tvd12.ezyfoxserver.builder.impl;

import java.net.InetSocketAddress;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.handler.EzyDataHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EzyServerBootstrapBuilderImpl implements EzyServerBootstrapBuilder {

	private int port;
	private EventLoopGroup childGroup;
	private EventLoopGroup parentGroup;
	private EzyBootstrap localBootstrap;
	private EzyCodecCreator codecCreator;
	
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
	public EzyServerBootstrapBuilder localBootstrap(EzyBootstrap localBootstrap) {
		this.localBootstrap = localBootstrap;
		return this;
	}
	
	@Override
	public EzyServerBootstrapBuilder codecCreator(EzyCodecCreator codecCreator) {
		this.codecCreator = codecCreator;
		return this;
	}
	
	public EzyServerBootstrap build() {
		EzyServerBootstrap answer = new EzyServerBootstrap();
		answer.setChildGroup(childGroup);
		answer.setParentGroup(parentGroup);
		answer.setLocalBootstrap(localBootstrap);
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
		return EzyChannelInitializer.builder().codecCreator(codecCreator).build();
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
	
	private EzyCodecCreator codecCreator;
	
	protected EzyChannelInitializer(Builder builder) {
		this.codecCreator = builder.codecCreator;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(codecCreator.newDecoder());
		pipeline.addLast(new EzyDataHandler());
		pipeline.addLast(codecCreator.newEncoder());
//		ch.config().setOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024));
		ch.config().setOption(ChannelOption.SO_RCVBUF, 2048);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private EzyCodecCreator codecCreator;
		
		public Builder codecCreator(EzyCodecCreator codecCreator) {
			this.codecCreator = codecCreator;
			return this;
		}
		
		public EzyChannelInitializer build() {
			return new EzyChannelInitializer(this);
		}
		
	}
}


