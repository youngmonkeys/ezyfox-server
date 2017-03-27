package com.tvd12.ezyfoxserver.builder.impl;

import java.net.InetSocketAddress;

import com.tvd12.ezyfoxserver.context.EzyServerContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public abstract class EzyAbstractBootstrapCreator<C extends EzyAbstractBootstrapCreator<C>> {

	protected int port;
	protected EzyServerContext context;
	protected EventLoopGroup childGroup;
	protected EventLoopGroup parentGroup;
	
	public C port(int port) {
		this.port = port;
		return getThis();
	}
	
	public C context(EzyServerContext context) {
		this.context = context;
		return getThis();
	}
	
	public C childGroup(EventLoopGroup childGroup) {
		this.childGroup = childGroup;
		return getThis();
	}
	
	public C parentGroup(EventLoopGroup parentGroup) {
		this.parentGroup = parentGroup;
		return getThis();
	}
	
	
	@SuppressWarnings("unchecked")
	protected C getThis() {
		return (C)this;
	}
	
	public ServerBootstrap create() {
		return newServerBootstrap()
				.group(parentGroup, childGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(newChannelInitializer());
	}
	
	protected ServerBootstrap newServerBootstrap() {
		return new ServerBootstrap() {
			@Override
			public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
				return childGroup != null 
						? super.group(parentGroup, childGroup) 
						: super.group(parentGroup);
			}
		};
	}
	
	protected abstract ChannelInitializer<Channel> newChannelInitializer();
	
}
