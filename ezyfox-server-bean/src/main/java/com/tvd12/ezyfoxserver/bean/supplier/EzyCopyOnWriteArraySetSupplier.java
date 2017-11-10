package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.concurrent.CopyOnWriteArraySet;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyCopyOnWriteArraySetSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new CopyOnWriteArraySet<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return CopyOnWriteArraySet.class;
	}
	
}
