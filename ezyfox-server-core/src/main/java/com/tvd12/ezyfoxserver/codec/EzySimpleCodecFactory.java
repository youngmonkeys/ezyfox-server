package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;

public class EzySimpleCodecFactory implements EzyCodecFactory {

	private final EzySocketSetting socketSettings;
	private final EzyWebSocketSetting websocketSettings;

	private final EzyCodecCreator socketCodecCreator;
	private final EzyCodecCreator websocketCodecCreator;
	
	public EzySimpleCodecFactory(Builder builder) {
		this.socketSettings = builder.socketSettings;
		this.websocketSettings = builder.websocketSettings;
		this.socketCodecCreator = newSocketCodecCreator();
		this.websocketCodecCreator = newWebsocketCodecCreator();
	}
	
	@Override
    public Object newEncoder(EzyConstant type) {
        return type == EzyConnectionType.SOCKET
                ? socketCodecCreator.newEncoder()
                : websocketCodecCreator.newEncoder();
    }
	
	@Override
	public Object newDecoder(EzyConstant type) {
		return type == EzyConnectionType.SOCKET
		        ? socketCodecCreator.newDecoder(socketSettings.getMaxRequestSize())
		        : websocketCodecCreator.newDecoder(websocketSettings.getMaxFrameSize());
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
			return new EzySimpleCodecFactory(this);
		}
	}
	
}
