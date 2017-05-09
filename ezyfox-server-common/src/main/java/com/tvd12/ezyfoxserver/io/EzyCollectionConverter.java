package com.tvd12.ezyfoxserver.io;

import java.util.Collection;

@SuppressWarnings({"rawtypes"})
public interface EzyCollectionConverter {
	
	<T> T toArray(Collection coll, Class type);
	
}
