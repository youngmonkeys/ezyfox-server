package com.tvd12.ezyfoxserver.bean.testing.circular;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class ClassC {

	@EzyAutoBind
	public ClassC(ClassB classB) {
	}
	
}
