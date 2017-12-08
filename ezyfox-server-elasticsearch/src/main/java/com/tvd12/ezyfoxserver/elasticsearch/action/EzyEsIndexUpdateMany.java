package com.tvd12.ezyfoxserver.elasticsearch.action;

import java.util.Arrays;

import org.apache.http.Header;
import org.elasticsearch.action.bulk.BulkResponse;

@SuppressWarnings("rawtypes")
public interface EzyEsIndexUpdateMany {

	BulkResponse indexUpdateMany(Object object, Header... headers);
	
	BulkResponse indexUpdateMany(Iterable objects, Header... headers);
	
	default BulkResponse indexUpdateMany(Object[] objects, Header... headers) {
		return indexUpdateMany(Arrays.asList(objects), headers);
	}
	
}
