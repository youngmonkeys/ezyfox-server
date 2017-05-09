package com.tvd12.ezyfoxserver.hazelcast;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzySimpleMapstoreCreator;

public class EzySimpleHazelcastFactory extends EzyAbstractHazelcastFactory {

	@Override
	protected EzyMapstoreCreator newMapstoreCreator() {
		return new EzySimpleMapstoreCreator();
	}

	
	
}
