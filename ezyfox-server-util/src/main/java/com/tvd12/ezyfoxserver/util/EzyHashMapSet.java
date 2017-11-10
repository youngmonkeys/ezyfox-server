package com.tvd12.ezyfoxserver.util;

import java.util.HashSet;
import java.util.Set;

public class EzyHashMapSet<K,E>
		extends EzyHashMapCollection<K, E, Set<E>>
		implements EzyMapSet<K, E> {
	private static final long serialVersionUID = 4067364721031740580L;

	@Override
	protected Set<E> newCollection() {
		return new HashSet<>();
	}
	
}
