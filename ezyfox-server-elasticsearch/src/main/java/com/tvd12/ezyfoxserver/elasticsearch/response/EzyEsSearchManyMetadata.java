package com.tvd12.ezyfoxserver.elasticsearch.response;

public interface EzyEsSearchManyMetadata extends EzyEsSearchMetadata {

	EzyEsHitsMetadata getHits();
	
}
