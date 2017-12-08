package com.tvd12.ezyfoxserver.elasticsearch.rest;

import com.tvd12.ezyfoxserver.elasticsearch.EzyEsRestHighLevelClientAware;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsOperation;

public interface EzyEsRestOperation
		extends EzyEsOperation, EzyEsRestHighLevelClientAware {

	<T> T execute();
	
}
