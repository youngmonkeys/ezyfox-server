package com.tvd12.ezyfoxserver.elasticsearch.rest;

import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;

import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexType;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexTypes;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexUpdateOneOperation;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetcher;

import lombok.Setter;

@Setter
@SuppressWarnings("unchecked")
public class EzyEsRestIndexUpdateOneOperation 
		extends EzyEsRestAbstractIndexUpdateOperation
		implements EzyEsIndexUpdateOneOperation {

	protected Object object;
	
	@Override
	public IndexResponse execute() {
		if(object == null)
			throw new NullPointerException("can't index null object");
		Class<?> objectType = object.getClass();
		EzyIdFetcher idFetcher = idFetchers.getIdFetcher(objectType);
		EzyIndexTypes indexTypes = indexedDataClasses.getIndexTypes(objectType);
		
		Object objectId = idFetcher.getId(object);
		EzyObject wrapper = marshaller.marshal(object);
		Map<String, Object> source = wrapper.toMap();
		
		EzyIndexType indexType = indexTypes.getIndexType();
		IndexRequest indexRequest = new IndexRequest()
				.id(objectId.toString())
				.index(indexType.getIndex())
				.type(indexType.getType())
				.source(source);
		try {
			IndexResponse response = highLevelClient.index(indexRequest, headers);
			return response;
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't index object: " + object, e);
		}
	}
	
}
