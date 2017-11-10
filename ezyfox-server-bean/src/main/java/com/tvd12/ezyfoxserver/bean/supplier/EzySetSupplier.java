package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzySetSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new HashSet<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return Set.class;
	}
	
}
