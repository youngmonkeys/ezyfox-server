package com.tvd12.ezyfoxserver.client.wrapper;

import com.tvd12.ezyfoxserver.client.listener.EzyClientAppResponseListener;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyClientAppResponseListeners extends EzyDestroyable {

	@SuppressWarnings("rawtypes")
	EzyClientAppResponseListener getListener(Object requestId);
	
	@SuppressWarnings("rawtypes")
	void addListener(Object requestId, EzyClientAppResponseListener listener);
	
}
