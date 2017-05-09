package com.tvd12.ezyfoxserver.bean.testing.combine2.pack1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class BImpl implements B {

	@EzyAutoBind
	public C c;
	
	@EzyAutoBind
	public D d;
	
}
