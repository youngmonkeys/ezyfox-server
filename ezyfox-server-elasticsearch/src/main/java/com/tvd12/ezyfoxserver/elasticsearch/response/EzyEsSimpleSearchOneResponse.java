package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.action.search.SearchResponse;

public class EzyEsSimpleSearchOneResponse<T>
		extends EzyEsSimpleResponse<EzyEsSearchOneMetadata, T, SearchResponse>
		implements EzyEsSearchOneResponse<T> {

}
