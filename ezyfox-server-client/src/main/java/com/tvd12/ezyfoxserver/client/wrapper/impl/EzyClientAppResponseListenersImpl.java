package com.tvd12.ezyfoxserver.client.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.listener.EzyClientAppResponseListener;
import com.tvd12.ezyfoxserver.client.wrapper.EzyClientAppResponseListeners;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyClientAppResponseListenersImpl 
		extends EzyLoggable implements EzyClientAppResponseListeners {

	@SuppressWarnings("rawtypes")
	protected Map<Object, EzyClientAppResponseListener> requests = new ConcurrentHashMap<>();
	
	protected EzyClientAppResponseListenersImpl(Builder builder) {
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public EzyClientAppResponseListener getListener(Object requestId) {
		return requests.get(requestId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addListener(Object requestId, EzyClientAppResponseListener listener) {
		requests.put(requestId, listener);
	}

	@Override
	public void destroy() {
		requests.clear();
		requests = null;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder implements EzyBuilder<EzyClientAppResponseListeners> {
		@Override
		public EzyClientAppResponseListeners build() {
			return new EzyClientAppResponseListenersImpl(this);
		}
	}
	
}
