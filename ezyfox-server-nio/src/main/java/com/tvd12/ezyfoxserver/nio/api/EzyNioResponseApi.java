package com.tvd12.ezyfoxserver.nio.api;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketResponseApi;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsResponseApi;
import com.tvd12.ezyfoxserver.response.EzyPackage;

public class EzyNioResponseApi implements EzyResponseApi {

	private EzyWsResponseApi websocketResponseApi;
	private EzyNioSocketResponseApi socketResponseApi;
	
	protected EzyNioResponseApi(Builder builder) {
		EzyCodecFactory codecFactory = builder.codecFactory;
		Object socketEncoder = codecFactory.newEncoder(EzyConnectionType.SOCKET);
		Object wsEncoder = codecFactory.newEncoder(EzyConnectionType.WEBSOCKET);
		this.socketResponseApi = new EzyNioSocketResponseApi(socketEncoder);
		this.websocketResponseApi = new EzyWsResponseApi(wsEncoder);
	}
	
	@Override
	public void response(EzyPackage pack, boolean immediate) throws Exception {
		socketResponseApi.response(pack, immediate);
		websocketResponseApi.response(pack, immediate);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyResponseApi> {
		
		private EzyCodecFactory codecFactory;
		
		public Builder codecFactory(EzyCodecFactory codecFactory) {
			this.codecFactory = codecFactory;
			return this;
		}
		
		public EzyResponseApi build() {
			return new EzyNioResponseApi(this);
		}
		
	}
	
}
