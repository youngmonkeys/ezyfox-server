package com.tvd12.ezyfoxserver.elasticsearch.response;

public interface EzyEsResponse<M,B,O> {

	B getBody();
	
	M getMetadata();
	
	O getOriginal();
}
