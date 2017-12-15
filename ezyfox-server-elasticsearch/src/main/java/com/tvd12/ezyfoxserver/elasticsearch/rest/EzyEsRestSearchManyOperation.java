package com.tvd12.ezyfoxserver.elasticsearch.rest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsSearchManyOperation;
import com.tvd12.ezyfoxserver.elasticsearch.response.EzyEsSimpleSearchManyMetadata;
import com.tvd12.ezyfoxserver.elasticsearch.response.EzyEsSimpleSearchManyResponse;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyEsRestSearchManyOperation
		extends EzyEsRestAbstractSearchOperation
		implements EzyEsSearchManyOperation {

	@Override
	protected Object newResponse(SearchResponse searchResponse) {
		EzyEsSimpleSearchManyResponse response = new EzyEsSimpleSearchManyResponse();
		EzyEsSimpleSearchManyMetadata metadata = new EzyEsSimpleSearchManyMetadata(searchResponse);
		response.setMetadata(metadata);
		response.setOriginal(searchResponse);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] array = hits.getHits();
		Object[] body = new Object[array.length];
		for(int i = 0 ; i < array.length ; i++) {
			body[i] = unmarshalHit(array[i]); 
		}
		response.setBody(body);
		return response;
	}

	
}
