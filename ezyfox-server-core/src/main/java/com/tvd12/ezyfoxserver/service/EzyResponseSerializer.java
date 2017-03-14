package com.tvd12.ezyfoxserver.service;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzyResponseSerializer {
	
	<T> T serialize(EzyResponse response, Class<T> outType);
	
	default EzyObject serializeToObject(EzyResponse response) {
		return serialize(response, EzyObject.class);
	}
	
	default EzyArray serializeToArray(EzyResponse response) {
		return serialize(response, EzyArray.class);
	}
	
}
