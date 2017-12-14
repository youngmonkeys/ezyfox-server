package com.tvd12.ezyfoxserver.elasticsearch.rest;

import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexTypes;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexedDataClasses;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsSearchOperation;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.io.EzyArrays;
import com.tvd12.ezyfoxserver.util.EzyEntityObjects;

import lombok.Setter;

@Setter
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class EzyEsRestAbstractSearchOperation
		extends EzyEsRestAbstractOperation
		implements EzyEsSearchOperation {

	protected Header[] headers;
	protected Class responseType;
	protected EzyIndexTypes indexTypes;
	protected SearchRequest searchRequest;
	protected EzyUnmarshaller unmarshaller;
	protected EzyIndexedDataClasses indexedDataClasses;
	
	@Override
	public Object execute() {
		this.prepare();
		try {
			SearchResponse searchResponse = highLevelClient.search(searchRequest, headers);
			Object entityResponse = newResponse(searchResponse);
			return entityResponse;
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't execute search request: " + searchRequest, e);
		}
	}
	
	protected void prepare() {
		if(indexTypes == null)
			indexTypes = indexedDataClasses.getIndexTypes(responseType);
		
		Set<String> indices = Sets.newHashSet(searchRequest.indices());
		if(indices.size() == 0) {
			indices = indexTypes.getIndexes();
			searchRequest.indices(EzyArrays.newArray(indices, String[]::new));
		}
		
		String[] types = searchRequest.types();
		if(types.length == 0) {
			for(String index : indices) {
				Set<String> tmp = indexTypes.getTypes(index);
				searchRequest.types(EzyArrays.newArray(tmp, String[]::new));
			}
		}
	}
	
	protected Object unmarshalHit(SearchHit hit) {
		Map<String, Object> map = hit.getSourceAsMap();
		EzyObject source = EzyEntityObjects.newObject(map);
		Object answer = unmarshaller.unmarshal(source, responseType);
		return answer;
	}
	
	protected abstract Object newResponse(SearchResponse searchResponse);
	
}
