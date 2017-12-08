package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.action.search.SearchResponse;

public class EzyEsSimpleSearchOneMetadata
		extends EzyEsSimpleSearchMetadata
		implements EzyEsSearchOneMetadata {
	private static final long serialVersionUID = 218866044524042849L;

	public EzyEsSimpleSearchOneMetadata(SearchResponse response) {
		super(response);
	}

	@Override
	public EzyEsHitMetadata getHit() {
		return hitsMetadata.getFirstHit();
	}
	
}
