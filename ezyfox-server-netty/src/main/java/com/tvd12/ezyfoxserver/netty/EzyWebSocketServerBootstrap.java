package com.tvd12.ezyfoxserver.netty;

import java.util.function.Function;

import javax.net.ssl.SSLContext;

import com.tvd12.ezyfoxserver.netty.builder.impl.EzyServerBootstrapCreator;
import com.tvd12.ezyfoxserver.netty.builder.impl.EzyWsServerBootstrapCreator;
import com.tvd12.ezyfoxserver.netty.builder.impl.EzyWsServerBootstrapCreatorFactory;
import com.tvd12.ezyfoxserver.netty.constant.EzyNettyThreadPoolSizes;
import com.tvd12.ezyfoxserver.netty.websocket.EzyWsWritingLoopHandler;
import com.tvd12.ezyfoxserver.setting.EzyBaseSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySslConfigSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;

public class EzyWebSocketServerBootstrap extends EzySocketServerBootstrap {

	private Function<EzySslConfigSetting, SSLContext> sslContextSupplier;
	
	protected EzyWebSocketServerBootstrap(Builder builder) {
		super(builder);
		this.sslContextSupplier = builder.sslContextSupplier;
	}
	
	@Override
	protected String getSocketType() {
		return "web socket";
	}
	
	@Override
	protected int getSocketWriterPoolSize() {
		return EzyNettyThreadPoolSizes.WEBSOCKET_WRITER;
	}
	
	private EzyWebSocketSetting getWebsocketSetting() {
        return getSettings().getWebsocket();
    }
	
	@Override
	protected EzyBaseSocketSetting getCommonSocketSetting() {
		return getWebsocketSetting();
	}
	
	@Override
	protected EzySocketEventLoopHandler newSocketWritingLoopHandler() {
		return new EzyWsWritingLoopHandler();
	}
	
	@Override
	protected EzyServerBootstrapCreator<?> newServerBootstrapCreator() {
		EzyWsServerBootstrapCreatorFactory factory = newWsServerBootstrapCreatorFactory();
		EzyWsServerBootstrapCreator<?> creator = factory.newCreator();
		return creator;
	}
	
	private EzyWsServerBootstrapCreatorFactory newWsServerBootstrapCreatorFactory() {
		EzyWebSocketSetting setting = getWebsocketSetting();
		SSLContext sslContext = sslContextSupplier.apply(setting.getSslConfig());
		return EzyWsServerBootstrapCreatorFactory.builder()
				.port(setting.getPort())
				.sslPort(setting.getSslPort())
				.sslActive(setting.isSslActive())
				.sslContext(sslContext)
				.wsCodecCreator(newCodecCreator())
				.maxFrameSize(setting.getMaxFrameSize())
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySocketServerBootstrap.Builder<Builder> {
		
		private Function<EzySslConfigSetting, SSLContext> sslContextSupplier;
		
		public Builder sslContextSupplier(
				Function<EzySslConfigSetting, SSLContext> sslContextSupplier) {
			this.sslContextSupplier = sslContextSupplier;
			return this;
		}
		
		@Override
		public EzyWebSocketServerBootstrap build() {
			return new EzyWebSocketServerBootstrap(this);
		}
		
	}
	
}
