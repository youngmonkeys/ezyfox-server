package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyLiteEntityBuilders;
import com.tvd12.test.base.BaseTest;

public class EzyLiteEntityBuildersTest extends BaseTest {

	@Test
	public void test() {
		new A().test();
	}
	
	public static class A extends EzyLiteEntityBuilders {
		
		public void test() {
			newArrayBuilder();
			newObjectBuilder();
		}
		
	}
}
