package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.callback.EzyCallback;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyWsDataDecoder extends EzyDestroyable {
	
	void decode(String bytes, EzyCallback<Object> callback) throws Exception;

	void decode(byte[] bytes, int offset, int len, EzyCallback<Object> callback) throws Exception;
	
}
