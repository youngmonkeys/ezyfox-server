package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyMethodFinder;
import com.tvd12.test.base.BaseTest;

public class EzyMethodFinderTest extends BaseTest {

	@Test
	public void test1() {
		Method m1 = EzyMethodFinder.builder()
			.clazz(B.class)
			.methodName("getName")
			.parameterTypes()
			.build()
			.find();
		assertNotNull(m1);
		
		Method m2 = EzyMethodFinder.builder()
				.clazz(B.class)
				.methodName("getName1")
				.parameterTypes()
				.build()
				.find();
		assertNotNull(m2);
		
		Method m3 = EzyMethodFinder.builder()
				.clazz(B.class)
				.methodName("getName2")
				.parameterTypes()
				.build()
				.find();
		assertNotNull(m3);
		
		Method m4 = EzyMethodFinder.builder()
				.clazz(B.class)
				.methodName("getName3")
				.parameterTypes()
				.build()
				.find();
		assertNull(m4);
	}
	
	public static interface I {
		String getName();
	}
	
	public static abstract class A implements I {
		public abstract String getName1();
	}
	
	public static abstract class B extends A {
		public abstract String getName2();
	}
	
}
