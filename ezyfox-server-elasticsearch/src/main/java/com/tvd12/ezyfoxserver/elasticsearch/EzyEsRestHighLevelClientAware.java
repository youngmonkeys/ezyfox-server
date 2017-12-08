package com.tvd12.ezyfoxserver.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;

public interface EzyEsRestHighLevelClientAware {

	void setHighLevelClient(RestHighLevelClient highLevelClient);
	
}
