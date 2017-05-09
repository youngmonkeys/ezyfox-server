package com.tvd12.ezyfoxserver.util;

import java.util.List;

public interface EzyMapList<K,E> extends EzyMapCollection<K, E, List<E>> {
	
	List<E> getItems(K key);
	
}
