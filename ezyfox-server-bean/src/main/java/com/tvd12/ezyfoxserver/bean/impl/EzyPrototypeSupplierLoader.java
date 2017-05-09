package com.tvd12.ezyfoxserver.bean.impl;

import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactory;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;

public interface EzyPrototypeSupplierLoader {

	EzyPrototypeSupplier load(EzyPrototypeFactory factory);
	
}
