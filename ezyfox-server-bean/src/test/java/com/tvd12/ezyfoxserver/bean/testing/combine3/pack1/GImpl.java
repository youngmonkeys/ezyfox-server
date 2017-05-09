package com.tvd12.ezyfoxserver.bean.testing.combine3.pack1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class GImpl implements G {

	@EzyAutoBind
	public GImpl(A a, E e, F f) {
	}
	
}
