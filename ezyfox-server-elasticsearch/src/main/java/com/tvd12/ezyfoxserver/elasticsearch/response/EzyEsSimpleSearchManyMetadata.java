package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.action.search.SearchResponse;

public class EzyEsSimpleSearchManyMetadata
		extends EzyEsSimpleSearchMetadata
		implements EzyEsSearchManyMetadata {
	private static final long serialVersionUID = 8697306736502028838L;

	public EzyEsSimpleSearchManyMetadata(SearchResponse response) {
		super(response);
	}

	@Override
	public EzyEsHitsMetadata getHits() {
		return hitsMetadata;
	}
	
}
