package com.tvd12.ezyfoxserver.elasticsearch;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public interface EzyEsRestClient extends EzyEsClient {

	RestHighLevelClient getHighLevelClient();
	
	default RestClient getLowLevelClient() {
		return getHighLevelClient().getLowLevelClient();
	}
	
}
