package com.tvd12.ezyfoxserver.bean.testing.configuration1;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.impl.EzySimpleConfigurationLoader;
import com.tvd12.test.base.BaseTest;

public class ConfigurationTest1 extends BaseTest {

	@Test
	public void test() {
		EzyBeanContext context = EzyBeanContext.builder()
				.scan("com.tvd12.ezyfoxserver.bean.testing.configuration1")
				.build();
		context.getSingletonFactory().addSingleton(new AvailableSingleton1());
		
		new EzySimpleConfigurationLoader()
				.clazz(ConfigClassA.class)
				.context(context)
				.load();
	}
	
}
