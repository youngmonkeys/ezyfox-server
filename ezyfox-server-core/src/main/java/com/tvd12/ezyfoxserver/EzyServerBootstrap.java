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
	protected ServerBootstrap wsServerBootstrap;
	@Setter
	private EzyBootstrap localBootstrap;
	
	private transient ChannelFuture channelFuture;
	private transient ChannelFuture wsChannelFuture;
	
	@Override
	public void start() throws Exception {
		startLocalBootstrap();
		startServerBootstrap();
		startWsServerBootstrap();
		notifyServerReady();
	}
	
	@Override
	public void destroy() {
		closeChannelFuture();
		closeWsChannelFuture();
		shutdownParentGroup();
		shutdownChildGroup();
	}
	
	protected void startLocalBootstrap() throws Exception {
		getLogger().debug("starting local bootstrap ....");
		localBootstrap.start();
		getLogger().debug("starting local bootstrap successful");
	}
	
	protected void startServerBootstrap() throws Exception {
		getLogger().debug("starting start bootstrap ....");
		this.channelFuture = serverBootstrap.bind().sync();
		getLogger().debug("starting server bootstrap successful");
	}
	
	protected void startWsServerBootstrap() throws Exception {
		getLogger().debug("starting ws server bootstrap ....");
		this.wsChannelFuture = wsServerBootstrap.bind().sync();
		getLogger().debug("starting ws server bootstrap successful");
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
	
	protected void closeWsChannelFuture() {
		try {
			this.wsChannelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			getLogger().error("close ws channel future error", e);
		}
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
	
	
}
