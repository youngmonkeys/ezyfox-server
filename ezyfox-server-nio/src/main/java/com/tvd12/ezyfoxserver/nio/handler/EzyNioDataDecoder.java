package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.callback.EzyCallback;
import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyNioDataDecoder extends EzyDestroyable {

	Object decode(EzyMessage message) throws Exception;
	
	void decode(byte[] bytes, EzyCallback<EzyMessage> callback) throws Exception;
	
}
