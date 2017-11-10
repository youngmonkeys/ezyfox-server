package com.tvd12.ezyfoxserver.bean.testing.combine.pack0;

import com.tvd12.ezyfoxserver.bean.annotation.EzyConfigurationBefore;

@EzyConfigurationBefore(priority = -1)
public class FirstConfiguration2 {

	public FirstConfiguration2() {
		System.out.println("\nHi! I will init before load bean, my priority = -1\n");
	}
	
}
