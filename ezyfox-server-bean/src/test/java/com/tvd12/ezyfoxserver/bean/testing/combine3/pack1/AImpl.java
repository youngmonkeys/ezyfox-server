package com.tvd12.ezyfoxserver.bean.testing.combine3.pack1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class AImpl implements A {

	@EzyAutoBind
	public AImpl(B b, C c, D d, E e, F f) {
	}
	
}
