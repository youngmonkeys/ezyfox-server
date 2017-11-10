package com.tvd12.ezyfoxserver.bean.testing.configuration2;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.impl.EzySimpleConfigurationLoader;
import com.tvd12.ezyfoxserver.bean.impl.EzySimplePrototypeSupplierLoader;
import com.tvd12.test.base.BaseTest;

public class ConfigurationTest1 extends BaseTest {

	@Test
	public void test() {
		EzySimplePrototypeSupplierLoader.setDebug(true);
		EzyBeanContext context = EzyBeanContext.builder()
				.scan("com.tvd12.ezyfoxserver.bean.testing.configuration2")
				.build();
		
		new EzySimpleConfigurationLoader()
				.clazz(ConfigClass1.class)
				.context(context)
				.load();
		
		context.getBean("p1", Prototype1.class);
		
		System.out.println("=========== second get =========");
		context.getBean("p1", Prototype1.class);
	}
	
}
