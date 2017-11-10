package com.tvd12.ezyfoxserver.bean.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.bean.impl.EzyBeanNameParser;
import com.tvd12.test.base.BaseTest;

public class EzyBeanNameParserTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyBeanNameParser.class;
	}
	
	@Test
	public void test() {
		assert EzyBeanNameParser.getBeanName(A.class).equals("x");
		assert EzyBeanNameParser.getBeanName(B.class).equals("y");
		assert EzyBeanNameParser.getBeanName(C.class).equals("c");
	}
	
	@EzyPrototype("x")
	public static class A {
		
	}
	
	@EzySingleton("y")
	public static class B {
		
	}
	
	public static class C {
		
	}
}
