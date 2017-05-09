package com.tvd12.ezyfoxserver.bean.testing.combine1;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.test.base.BaseTest;

public class Combine1Test extends BaseTest {

	@Test
	public void test() {
		EzyBeanContext context = EzyBeanContext.builder()
				.scan("com.tvd12.ezyfoxserver.bean.testing.combine1")
				.build();
		Singleton1 s1 = (Singleton1)context.getBean(Singleton1.class);
		assert s1.getSingleton0() == context.getBean(Singleton0.class);
		assert s1.getSingleton2() == context.getBean(Singleton2.class);
	}
	
}
