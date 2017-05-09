package com.tvd12.ezyfoxserver.nio.builder.impl;

import static com.tvd12.ezyfoxserver.constant.EzyConnectionType.SOCKET;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.factory.EzyCodecFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioObjectToByteEncoder;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;

public class EzyCodecFactoryImpl implements EzyCodecFactory {

	private final EzySocketSetting socketSettings;
	private final EzyWebSocketSetting websocketSettings;

	private final EzyCodecCreator socketCodecCreator;
	private final EzyCodecCreator websocketCodecCreator;
	
	public EzyCodecFactoryImpl(Builder builder) {
		this.socketSettings = builder.socketSettings;
		this.websocketSettings = builder.websocketSettings;
		this.socketCodecCreator = newSocketCodecCreator();
		this.websocketCodecCreator = newWebsocketCodecCreator();
	}
	
	@Override
	public Object newDecoder(EzyConnectionType type) {
		if(type == SOCKET) {
			return socketCodecCreator.newDecoder(socketSettings.getMaxRequestSize());
		}
		return websocketCodecCreator.newDecoder(websocketSettings.getMaxFrameSize());
	}
	
	@Override
	public Object newEncoder(EzyConnectionType type) {
		if(type == SOCKET) {
			return (EzyNioObjectToByteEncoder) socketCodecCreator.newEncoder();
		}
		return (EzyNioObjectToByteEncoder) websocketCodecCreator.newEncoder();
	}
	
	private EzyCodecCreator newSocketCodecCreator() {
		return EzyClasses.newInstance(socketSettings.getCodecCreator());
	}
	
	private EzyCodecCreator newWebsocketCodecCreator() {
		return EzyClasses.newInstance(websocketSettings.getCodecCreator());
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyCodecFactory> {
		private EzySocketSetting socketSettings;
		private EzyWebSocketSetting websocketSettings;
		
		public Builder socketSettings(EzySocketSetting settings) {
			this.socketSettings = settings;
			return this;
		}
		
		public Builder websocketSettings(EzyWebSocketSetting settings) {
			this.websocketSettings = settings;
			return this;
		}
		
		@Override
		public EzyCodecFactory build() {
			return new EzyCodecFactoryImpl(this);
		}
	}
	
}
