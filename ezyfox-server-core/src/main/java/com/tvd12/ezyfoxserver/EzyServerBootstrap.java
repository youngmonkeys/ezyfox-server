package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyServerReadyEventImpl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import lombok.Setter;

public class EzyServerBootstrap implements EzyStartable, EzyDestroyable {
	
	@Setter
	protected EzyServerContext context;
	@Setter
	protected EventLoopGroup childGroup;
	@Setter
	protected EventLoopGroup parentGroup;
	@Setter
	protected ServerBootstrap serverBootstrap;
	@Setter
	private EzyBootstrap localBootstrap;
	
	private transient ChannelFuture channelFuture;
	
	@Override
	public void start() throws Exception {
		startLocalBootstrap();
		startServerBootstrap();
		notifyServerReady();
	}
	
	@Override
	public void destroy() {
		closeChannelFuture();
		shutdownParentGroup();
		shutdownChildGroup();
	}
	
	protected void startServerBootstrap() throws Exception {
		this.channelFuture = serverBootstrap.bind().sync();
	}
	
	protected void startLocalBootstrap() throws Exception {
		localBootstrap.start();
	}
	
	protected void notifyServerReady() {
		context.get(EzyFireEvent.class).fire(EzyEventType.SERVER_READY, newServerReadyEvent());
	}
	
	protected EzyEvent newServerReadyEvent() {
		return EzyServerReadyEventImpl.builder().server(context.getBoss()).build();
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
