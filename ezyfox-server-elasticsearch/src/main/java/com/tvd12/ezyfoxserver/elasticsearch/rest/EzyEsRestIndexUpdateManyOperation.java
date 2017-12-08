package com.tvd12.ezyfoxserver.elasticsearch.rest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;

import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexTypes;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexUpdateManyOperation;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetcher;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EzyEsRestIndexUpdateManyOperation 
		extends EzyEsRestAbstractIndexUpdateOperation
		implements EzyEsIndexUpdateManyOperation {

	protected Set<Object> objects = new HashSet<>();
	
	@Override
	public void setObject(Object object) {
		this.objects.add(object);
	}
	
	@Override
	public void setObjects(Iterable objects) {
		objects.forEach(this::setObject);
	}
	
	@Override
	public BulkResponse execute() {
		BulkRequest bulkRequest = new BulkRequest();
		for(Object object : objects) {
			Class<?> objectType = object.getClass();
			EzyIdFetcher idFetcher = idFetchers.getIdFetcher(objectType);
			EzyIndexTypes indexTypes = indexedDataClasses.getIndexTypes(objectType);
			
			Object objectId = idFetcher.getId(object);
			EzyObject wrapper = marshaller.marshal(object);
			Map<String, Object> source = wrapper.toMap();
			
			for(String index : indexTypes.getIndexes()) {
				for(String type : indexTypes.getTypes(index)) {
					IndexRequest indexRequest = new IndexRequest(index)
							.id(objectId.toString())
							.type(type)
							.source(source);
					bulkRequest.add(indexRequest);
				}
			}
		}
		try {
			BulkResponse response = highLevelClient.bulk(bulkRequest, headers);
			return response;
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't index objects: " + objects, e);
		}
	}
	
}
