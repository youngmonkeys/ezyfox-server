package com.tvd12.ezyfoxserver.bean.testing.combine.pack3;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class Pack3A1Impl implements Pack3A1 {

	@EzyAutoBind
	public Pack3A2 pack3A2;
	
}
