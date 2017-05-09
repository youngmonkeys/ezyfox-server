package com.tvd12.ezyfoxserver.hazelcast.transaction;

public interface EzyTransaction {

	void begin();
	
	void commit();
	
	void rollback();
	
}
