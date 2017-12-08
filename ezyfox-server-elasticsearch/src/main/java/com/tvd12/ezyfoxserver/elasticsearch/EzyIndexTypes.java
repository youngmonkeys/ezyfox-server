package com.tvd12.ezyfoxserver.elasticsearch;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.util.EzyHashMapSet;
import com.tvd12.ezyfoxserver.util.EzyMapSet;

public class EzyIndexTypes implements Serializable {
	private static final long serialVersionUID = 8736647948412035159L;
	
	protected EzyMapSet<String, String> indexTypes = new EzyHashMapSet<>();
	
	protected EzyIndexTypes(Builder builder) {
		this.indexTypes = builder.indexTypes;
	}
	
	public String getIndex() {
		return indexTypes.keySet().iterator().next();
	}
	
	public String getType() {
		String index = getIndex();
		Set<String> types = getTypes(index);
		return types.iterator().next();
	}
	
	public EzyIndexType getIndexType() {
		String index = getIndex();
		String type = getType();
		return new EzyIndexType(index, type);
	}
	
	public Set<String> getIndexes() {
		return new HashSet<>(indexTypes.keySet());
	}
	
	public Set<String> getTypes(String index) {
		return new HashSet<>(indexTypes.getItems(index));
	}
	
	public Set<EzyIndexType> getIndexTypes() {
		Set<EzyIndexType> set = new HashSet<>();
		for(String index : indexTypes.keySet()) {
			Set<String> types = indexTypes.getItems(index);
			for(String type : types) {
				set.add(new EzyIndexType(index, type));
			}
		}
		return set;
	}
	
	@Override
	public String toString() {
		return indexTypes.toString();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyIndexTypes> {

		protected EzyMapSet<String, String> indexTypes = new EzyHashMapSet<>();
		
		public Builder add(EzyIndexType indexType) {
			this.indexTypes.addItems(indexType.getIndex(), indexType.getType());
			return this;
		}
		
		public Builder add(EzyIndexTypes indexTypes) {
			return add(indexTypes.getIndexTypes());
		}
		
		public Builder add(String index, String type) {
			return add(new EzyIndexType(index, type));
		}
		
		public Builder add(Iterable<EzyIndexType> indexTypes) {
			indexTypes.forEach(this::add);
			return this;
		}
		
		@Override
		public EzyIndexTypes build() {
			return new EzyIndexTypes(this);
		}
		
	}

}
