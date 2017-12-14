package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.action.search.SearchResponse;

public class EzyEsSimpleSearchManyResponse<T>
		extends EzyEsSimpleResponse<EzyEsSearchManyMetadata, T, SearchResponse>
		implements EzyEsSearchManyResponse<T> {

}
