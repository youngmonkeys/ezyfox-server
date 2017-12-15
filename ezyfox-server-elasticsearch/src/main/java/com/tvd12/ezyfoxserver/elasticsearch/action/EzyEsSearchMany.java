package com.tvd12.ezyfoxserver.elasticsearch.action;

import org.apache.http.Header;

import com.tvd12.ezyfoxserver.elasticsearch.response.EzyEsSearchManyResponse;

public interface EzyEsSearchMany {

	<T> EzyEsSearchManyResponse<T> searchMany(Object input, Class<T> responseType, Header... headers);
	
}
