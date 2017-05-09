package com.tvd12.ezyfoxserver.hazelcast;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextAware;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyBeanMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;

import lombok.Setter;

public class EzyBeanHazelcastFactory 
		extends EzyAbstractHazelcastFactory
		implements EzyBeanContextAware {
	
	@Setter
	protected EzyBeanContext context;

	@Override
	protected EzyMapstoreCreator newMapstoreCreator() {
		EzyBeanMapstoreCreator creator = new EzyBeanMapstoreCreator();
		creator.setContext(context);
		return creator;
	}

}
