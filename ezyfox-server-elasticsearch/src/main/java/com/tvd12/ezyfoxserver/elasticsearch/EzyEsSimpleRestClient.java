package com.tvd12.ezyfoxserver.elasticsearch;

import org.apache.http.Header;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexUpdateManyOperation;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexUpdateOneOperation;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexUpdateOperation;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsOperation;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsSearchOperation;
import com.tvd12.ezyfoxserver.elasticsearch.response.EzyEsSearchOneResponse;
import com.tvd12.ezyfoxserver.elasticsearch.rest.EzyEsRestIndexUpdateManyOperation;
import com.tvd12.ezyfoxserver.elasticsearch.rest.EzyEsRestIndexUpdateOneOperation;
import com.tvd12.ezyfoxserver.elasticsearch.rest.EzyEsRestOperation;
import com.tvd12.ezyfoxserver.elasticsearch.rest.EzyEsRestSearchOneOperation;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetchers;
import com.tvd12.ezyfoxserver.identifier.EzyIdSetters;

import lombok.Setter;

@Setter
@SuppressWarnings({"rawtypes"})
public class EzyEsSimpleRestClient implements EzyEsRestClient {

	protected EzyIdSetters idSetters;
	protected EzyMarshaller marshaller;
	protected EzyIdFetchers idFetchers;
	protected EzyUnmarshaller unmarshaller;
	protected RestHighLevelClient highLevelClient;
	protected EzyIndexedDataClasses indexedDataClasses;
	
	@Override
	public RestHighLevelClient getHighLevelClient() {
		return highLevelClient;
	}
	
	@Override
	public BulkResponse indexUpdateOne(Object object, Header... headers) {
		EzyEsIndexUpdateOneOperation operation = new EzyEsRestIndexUpdateOneOperation();
		operation.setObject(object);
		return executeIndexUpdateOperation(operation, headers);
	}
	
	@Override
	public BulkResponse indexUpdateMany(Object object, Header... headers) {
		EzyEsIndexUpdateManyOperation operation = new EzyEsRestIndexUpdateManyOperation();
		operation.setObject(object);
		return executeIndexUpdateOperation(operation, headers);
	}
	
	@Override
	public BulkResponse indexUpdateMany(Iterable objects, Header... headers) {
		EzyEsIndexUpdateManyOperation operation = new EzyEsRestIndexUpdateManyOperation();
		operation.setObjects(objects);
		return executeIndexUpdateOperation(operation, headers);
	}
	
	@Override
	public <T> EzyEsSearchOneResponse<T> searchOne(
			Object input, Class<T> responseType, Header... headers) {
		SearchRequest searchRequest = (SearchRequest)input;
		EzyEsRestSearchOneOperation operation = new EzyEsRestSearchOneOperation();
		operation.setResponseType(responseType);
		operation.setSearchRequest(searchRequest);
		return executeSearchOperation(operation, searchRequest, responseType, headers);
	}
	
	protected <T> T executeOperation(EzyEsOperation operation) {
		EzyEsRestOperation restOperation = (EzyEsRestOperation)operation;
		restOperation.setHighLevelClient(highLevelClient);
		T answer = operation.execute();
		return answer;
	}
	
	protected <T> T executeSearchOperation(
			EzyEsSearchOperation operation, 
			SearchRequest searchRequest, Class responseType, Header...headers) {
		operation.setHeaders(headers);
		operation.setIdSetters(idSetters);
		operation.setUnmarshaller(unmarshaller);
		operation.setResponseType(responseType);
		operation.setIndexedDataClasses(indexedDataClasses);
		return executeOperation(operation);
	}
	
	protected <T> T executeIndexUpdateOperation(EzyEsIndexUpdateOperation operation, Header...headers) {
		operation.setHeaders(headers);
		operation.setMarshaller(marshaller);
		operation.setIdFetchers(idFetchers);
		operation.setIndexedDataClasses(indexedDataClasses);
		return executeOperation(operation);
	}
	
}
