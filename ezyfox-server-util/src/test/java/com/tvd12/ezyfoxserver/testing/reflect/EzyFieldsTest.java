package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyFields;
import com.tvd12.test.base.BaseTest;

public class EzyFieldsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyFields.class;
	}
	
	@Test
	public void test() {
		assertEquals(EzyFields.getAnnotatedFields(ClassB.class, ExampleAnnotation.class).size(), 2);
	}
	
	public static class ClassA {
		@ExampleAnnotation
		String value;
	}
	
	public static class ClassB extends ClassA {
		@ExampleAnnotation
		String name;
	}
	
}
