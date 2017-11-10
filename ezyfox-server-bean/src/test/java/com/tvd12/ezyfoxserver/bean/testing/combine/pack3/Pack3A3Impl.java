package com.tvd12.ezyfoxserver.bean.testing.combine.pack3;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton
public class Pack3A3Impl implements Pack3A3 {

	@EzyAutoBind
	public Pack3A4 pack3A4;
	
}
