package com.tvd12.ezyfoxserver.bean.supplier;

import java.util.ArrayList;
import java.util.Collection;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public class EzyCollectionSupplier implements EzyPrototypeSupplier {

	@Override
	public Object supply(EzyBeanContext context) {
		return new ArrayList<>();
	}
	
	@Override
	public Class<?> getObjectType() {
		return Collection.class;
	}
	
}
