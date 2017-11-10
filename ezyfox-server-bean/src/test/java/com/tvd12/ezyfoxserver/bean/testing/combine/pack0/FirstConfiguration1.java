package com.tvd12.ezyfoxserver.bean.testing.combine.pack0;

import com.tvd12.ezyfoxserver.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfoxserver.bean.testing.combine.EzyCombine0Ann;

@EzyCombine0Ann
@EzyConfigurationBefore(priority = 0)
public class FirstConfiguration1 {

	public FirstConfiguration1() {
		System.out.println("\nHi! I will init before load bean, my priority = 0\n");
	}
	
}
