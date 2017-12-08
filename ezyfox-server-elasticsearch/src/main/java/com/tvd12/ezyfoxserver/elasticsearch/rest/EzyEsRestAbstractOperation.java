package com.tvd12.ezyfoxserver.elasticsearch.rest;

import org.elasticsearch.client.RestHighLevelClient;

import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

public abstract class EzyEsRestAbstractOperation 
		extends EzyLoggable 
		implements EzyEsRestOperation {

	@Setter
	protected RestHighLevelClient highLevelClient;
	
}
