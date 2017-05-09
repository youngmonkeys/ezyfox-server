package com.tvd12.ezyfoxserver.bean.testing.combine2.pack1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class DImpl implements D {

	@EzyAutoBind
	public C c;
}
