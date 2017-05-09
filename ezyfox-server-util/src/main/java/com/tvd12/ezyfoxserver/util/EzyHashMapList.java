package com.tvd12.ezyfoxserver.util;

import java.util.ArrayList;
import java.util.List;

public class EzyHashMapList<K,E>
		extends EzyHashMapCollection<K, E, List<E>>
		implements EzyMapList<K, E> {
	private static final long serialVersionUID = 3678081740856760565L;

	@Override
	protected List<E> newCollection() {
		return new ArrayList<>();
	}
	
}
