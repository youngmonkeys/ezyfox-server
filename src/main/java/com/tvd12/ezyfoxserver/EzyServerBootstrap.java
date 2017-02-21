package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyDestroyable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import lombok.Setter;

public class EzyServerBootstrap implements EzyDestroyable {

	@Setter
	private EventLoopGroup childGroup;
	@Setter
	private EventLoopGroup parentGroup;
	@Setter
	private ServerBootstrap serverBootstrap;
	
	private transient ChannelFuture channelFuture;
	
	public void start() throws Exception {
		this.channelFuture = serverBootstrap.bind().sync();
	}
	
	@Override
	public void destroy() {
		closeChannelFuture();
		shutdownParentGroup();
		shutdownChildGroup();
	}
	
	protected void shutdownChildGroup() {
		try {
			if(this.childGroup != null)
				this.childGroup.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			getLogger().error("shutdown child group error", e);
		}
	}
	
	protected void shutdownParentGroup() {
		try {
			this.parentGroup.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			getLogger().error("shutdown parent group error", e);
		}
	}
	
	protected void closeChannelFuture() {
		try {
			this.channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			getLogger().error("close channel future error", e);
		}
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
	
	
}
