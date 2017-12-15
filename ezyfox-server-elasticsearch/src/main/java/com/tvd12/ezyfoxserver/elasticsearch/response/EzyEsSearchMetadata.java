package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.common.unit.TimeValue;

public interface EzyEsSearchMetadata extends EzyEsMetadata {

	boolean isTimedOut();

	Boolean isTerminatedEarly();

	int getNumReducePhases();

	TimeValue getTook();

	int getTotalShards();

	int getSuccessfulShards();

	int getSkippedShards();

	int getFailedShards();

	String getScrollId();

	static interface EzyEsHitsMetadata extends EzyEsMetadata {

		long getTotalHits();

		double getMaxScore();

		EzyEsHitMetadata[] getHits();

		EzyEsHitMetadata getFirstHit();

		EzyEsHitMetadata getHitAt(int index);
	}

	static interface EzyEsHitMetadata extends EzyEsMetadata {

		double getScore();

		String getType();

		long getVersion();

	}

}
