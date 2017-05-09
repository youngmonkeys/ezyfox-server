package com.tvd12.ezyfoxserver.hazelcast.testing;

import org.testng.annotations.Test;

import com.hazelcast.config.Config;
import com.tvd12.ezyfoxserver.hazelcast.EzySimpleConfigCustomizer;
import com.tvd12.test.base.BaseTest;

public class EzySimpleConfigCustomizerTest extends BaseTest {

	@Test
	public void test() {
		new EzySimpleConfigCustomizer().customize(new Config());
	}
	
}
