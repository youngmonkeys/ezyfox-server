package com.tvd12.ezyfoxserver.bean.testing.circular;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class ClassA {

	@EzyAutoBind
	public ClassA(ClassC classC) {
		
	}
	
}
