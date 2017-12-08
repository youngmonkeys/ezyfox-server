package com.tvd12.ezyfoxserver.elasticsearch.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class EzyEsSimpleSearchMetadata implements EzyEsSearchMetadata {
	private static final long serialVersionUID = -3575239468004123967L;
	
	protected final SearchResponse response;
	protected final EzyEsHitsMetadata hitsMetadata;
	
	public EzyEsSimpleSearchMetadata(SearchResponse response) {
		this.response = response;
		this.hitsMetadata = new EzyEsSimpleHitsMetadata(response.getHits());
	}
	
	@Override
	public boolean isTimedOut() {
		return response.isTimedOut();
	}

	@Override
	public Boolean isTerminatedEarly() {
		return response.isTerminatedEarly();
	}

	@Override
	public int getNumReducePhases() {
		return response.getNumReducePhases();
	}

	@Override
	public TimeValue getTook() {
		return response.getTook();
	}

	@Override
	public int getTotalShards() {
		return response.getTotalShards();
	}

	@Override
	public int getSuccessfulShards() {
		return response.getSuccessfulShards();
	}

	@Override
	public int getSkippedShards() {
		return response.getSkippedShards();
	}

	@Override
	public int getFailedShards() {
		return response.getFailedShards();
	}

	@Override
	public String getScrollId() {
		return response.getScrollId();
	}

	public static class EzyEsSimpleHitsMetadata implements EzyEsHitsMetadata {
		private static final long serialVersionUID = 5394112349587614736L;
		
		protected final SearchHits hits;
		protected final EzyEsHitMetadata[] hitMetadatas;
		
		public EzyEsSimpleHitsMetadata(SearchHits hits) {
			this.hits = hits;
			SearchHit[] array = hits.getHits();
			this.hitMetadatas = new EzyEsHitMetadata[array.length];
			for(int i = 0 ; i < array.length ; i++)
				this.hitMetadatas[i] = new EzyEsSimpleHitMetadata(array[i]);
		}
		
		@Override
		public long getTotalHits() {
			return this.hits.totalHits;
		}
		
		@Override
		public double getMaxScore() {
			return this.hits.getMaxScore();
		}
		
		@Override
		public EzyEsHitMetadata[] getHits() {
			return this.hitMetadatas;
		}
		
		@Override
		public EzyEsHitMetadata getFirstHit() {
			return hitMetadatas.length > 0 ? hitMetadatas[0] : null;
		}
		
		@Override
		public EzyEsHitMetadata getHitAt(int index) {
			return this.hitMetadatas[index];
		}
		
	}
	
	public static class EzyEsSimpleHitMetadata implements EzyEsHitMetadata {
		private static final long serialVersionUID = 8525262613868298675L;

		protected final SearchHit hit;
		
		public EzyEsSimpleHitMetadata(SearchHit hit) {
			this.hit = hit;
		}

		@Override
		public double getScore() {
			return hit.getScore();
		}

		@Override
		public String getType() {
			return hit.getType();
		}

		@Override
		public long getVersion() {
			return hit.getVersion();
		}
		
	}
	
}
