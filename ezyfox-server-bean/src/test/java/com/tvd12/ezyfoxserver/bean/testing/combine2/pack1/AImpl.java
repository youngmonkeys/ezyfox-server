package com.tvd12.ezyfoxserver.bean.testing.combine2.pack1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Setter;

@EzySingleton
public class AImpl implements A {

	@EzyAutoBind
	public B b;
	
	@EzyAutoBind
	public C c;
	
	@EzyAutoBind
	public D d;
	
	@Setter
	@EzyAutoBind
	private F f;
	
	@EzyAutoBind
	public G g;
	
}
