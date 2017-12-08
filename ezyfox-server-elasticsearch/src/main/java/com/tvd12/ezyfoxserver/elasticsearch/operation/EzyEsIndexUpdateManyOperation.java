package com.tvd12.ezyfoxserver.elasticsearch.operation;

@SuppressWarnings("rawtypes")
public interface EzyEsIndexUpdateManyOperation extends EzyEsIndexUpdateOperation {

	void setObject(Object object);
	
	void setObjects(Iterable objects);
	
}
