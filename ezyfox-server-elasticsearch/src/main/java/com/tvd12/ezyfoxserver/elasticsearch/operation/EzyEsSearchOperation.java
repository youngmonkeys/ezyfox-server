package com.tvd12.ezyfoxserver.elasticsearch.operation;

import org.apache.http.Header;
import org.elasticsearch.action.search.SearchRequest;

import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexTypes;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexedDataClasses;

@SuppressWarnings("rawtypes")
public interface EzyEsSearchOperation extends EzyEsOperation {
	
	void setHeaders(Header[] headers);
	
	void setResponseType(Class responseType);

	void setIndexTypes(EzyIndexTypes indexTypes);
	
	void setUnmarshaller(EzyUnmarshaller unmarshaller);
	
	void setSearchRequest(SearchRequest searchRequest);
	
	void setIndexedDataClasses(EzyIndexedDataClasses indexedDataClasses);
	
}
