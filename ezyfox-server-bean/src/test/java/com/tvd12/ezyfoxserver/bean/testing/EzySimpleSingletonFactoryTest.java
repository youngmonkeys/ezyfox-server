package com.tvd12.ezyfoxserver.bean.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.impl.EzySimpleSingletonFactory;
import com.tvd12.test.base.BaseTest;

public class EzySimpleSingletonFactoryTest extends BaseTest {

	@Test
	public void test() {
		EzySimpleSingletonFactory factory = new EzySimpleSingletonFactory();
		factory.addSingleton("a", new A());
		assert factory.getSingleton("a", A1.class) == null;
	}
	
	public static class A {
		
	}
	
	public static class A1 {
		
	}
	
}
