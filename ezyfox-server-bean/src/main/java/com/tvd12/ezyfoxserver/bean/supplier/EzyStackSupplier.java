package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.Stack;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyStackSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new Stack<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return Stack.class;
	}
	
}
