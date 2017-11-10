package com.tvd12.ezyfoxserver.io;

public final class EzyIterables {

	private EzyIterables() {
	}
	
	public static boolean isEmpty(Iterable<?> iterable) {
		if(iterable == null) 
			return true;
		return !iterable.iterator().hasNext();
	}
	
}
