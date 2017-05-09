package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyLoggableTest extends EzyLoggable {

	@Test
	public void test() {
		getLogger().debug("abc");
	}
	
}
