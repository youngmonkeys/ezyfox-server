package com.tvd12.ezyfoxserver.elasticsearch;

import com.tvd12.ezyfoxserver.elasticsearch.action.EzyEsIndexActions;
import com.tvd12.ezyfoxserver.elasticsearch.action.EzyEsSearchActions;

public interface EzyEsClient extends 
		EzyEsIndexActions,
		EzyEsSearchActions {
}
