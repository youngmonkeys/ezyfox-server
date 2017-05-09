package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyConcurrentHashMapSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new ConcurrentHashMap<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return ConcurrentHashMap.class;
	}
	
}
