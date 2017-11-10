package com.tvd12.ezyfoxserver.bean.testing.combine2.pack1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class EImpl implements E {

	@EzyAutoBind
	public A a;
	
	@EzyAutoBind
	public B b;
	
	@EzyAutoBind
	public C c;
	
	@EzyAutoBind
	public D d;
	
}
