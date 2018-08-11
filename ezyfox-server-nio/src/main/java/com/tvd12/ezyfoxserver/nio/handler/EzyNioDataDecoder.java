package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.callback.EzyCallback;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.util.EzyDestroyable;

public interface EzyNioDataDecoder extends EzyDestroyable {

	Object decode(EzyMessage message) throws Exception;
	
	void decode(byte[] bytes, EzyCallback<EzyMessage> callback) throws Exception;
	
}
