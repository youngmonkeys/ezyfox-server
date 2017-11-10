package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.HashMap;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyHashMapSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new HashMap<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return HashMap.class;
	}
	
}
