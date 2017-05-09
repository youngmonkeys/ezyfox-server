package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.concurrent.CopyOnWriteArrayList;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyCopyOnWriteArrayListSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new CopyOnWriteArrayList<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return CopyOnWriteArrayList.class;
	}
	
}
