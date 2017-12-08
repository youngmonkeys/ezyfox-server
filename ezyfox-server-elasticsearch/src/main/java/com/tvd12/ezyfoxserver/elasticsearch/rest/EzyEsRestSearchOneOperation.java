package com.tvd12.ezyfoxserver.elasticsearch.rest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsSearchOneOperation;
import com.tvd12.ezyfoxserver.elasticsearch.response.EzyEsSimpleSearchOneMetadata;
import com.tvd12.ezyfoxserver.elasticsearch.response.EzyEsSimpleSearchOneResponse;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyEsRestSearchOneOperation
		extends EzyEsRestAbstractSearchOperation
		implements EzyEsSearchOneOperation {

	@Override
	protected Object newResponse(SearchResponse searchResponse) {
		EzyEsSimpleSearchOneResponse response = new EzyEsSimpleSearchOneResponse();
		EzyEsSimpleSearchOneMetadata metadata = new EzyEsSimpleSearchOneMetadata(searchResponse);
		response.setMetadata(metadata);
		response.setOriginal(searchResponse);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] array = hits.getHits();
		if(array.length > 0) {
			Object body = unmarshalHit(array[0]);
			response.setBody(body);
		}
		return response;
	}

	
}
