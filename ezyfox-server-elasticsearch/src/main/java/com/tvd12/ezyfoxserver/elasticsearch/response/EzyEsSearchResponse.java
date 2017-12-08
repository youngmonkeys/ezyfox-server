package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.action.search.SearchResponse;

public interface EzyEsSearchResponse<M extends EzyEsSearchMetadata, B> 
		extends EzyEsResponse<M, B, SearchResponse> {
}
