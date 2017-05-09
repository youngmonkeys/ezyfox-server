package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.LinkedList;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyLinkedListSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new LinkedList<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return LinkedList.class;
	}
	
}
