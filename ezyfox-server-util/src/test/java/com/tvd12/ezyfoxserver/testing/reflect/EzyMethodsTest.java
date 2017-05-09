package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyMethods;
import com.tvd12.test.base.BaseTest;

public class EzyMethodsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyMethods.class;
	}
	
	@Test
	public void test() {
		assertNotNull(EzyMethods.getPublicMethod(ClassB.class, "setValue", String.class));
		assertEquals(EzyMethods.getAnnotatedMethods(ClassB.class, ExampleAnnotation.class).size(), 1);
	}
	
	public static class ClassA {
		
		public void setValue(String value) {
		}
		
	}
	
	public static class ClassB extends ClassA {
		
		@ExampleAnnotation
		public void setName(String name) {
			
		}
	}
	
}
