package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.response.EzyPackage;

public class EzyProxyResponseApi implements EzyResponseApi {

	private final EzyWsResponseApi websocketResponseApi;
	private final EzySocketResponseApi socketResponseApi;
	
	public EzyProxyResponseApi(EzyCodecFactory codecFactory) {
		Object socketEncoder = codecFactory.newEncoder(EzyConnectionType.SOCKET);
		Object wsEncoder = codecFactory.newEncoder(EzyConnectionType.WEBSOCKET);
		this.socketResponseApi = new EzySocketResponseApi(socketEncoder);
		this.websocketResponseApi = new EzyWsResponseApi(wsEncoder);
	}
	
	@Override
	public void response(EzyPackage pack, boolean immediate) throws Exception {
		socketResponseApi.response(pack, immediate);
		websocketResponseApi.response(pack, immediate);
	}
	
}
