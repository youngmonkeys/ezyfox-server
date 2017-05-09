package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;
import com.tvd12.test.base.BaseTest;

public class EzySetterMethodTest extends BaseTest {

	@Test
	public void test() throws Exception {
		EzySetterMethod setter = new EzySetterMethod(
				ClassA.class.getDeclaredMethod("setValue", String.class));
		assertEquals(setter.getType(), String.class);
		assertEquals(setter.getGenericType(), String.class);
		assertEquals(setter.isMapType(), false);
		assertEquals(setter.isCollection(), false);
		assertEquals(setter.getFieldName(), "value");
		assert setter.getParameterCount() >= 0;
	}
	
	@Test
	public void test2() throws Exception {
		EzySetterMethod setter = new EzySetterMethod(
				ClassA.class.getDeclaredMethod("setA", String.class));
		assertEquals(setter.getFieldName(), "a");
	}
	
	@Test
	public void test3() throws Exception {
		EzySetterMethod setter = new EzySetterMethod(
				ClassA.class.getDeclaredMethod("duplicateValue", String.class));
		assertEquals(setter.getFieldName(), "duplicateValue");
	}
	
	@Test
	public void test4() throws Exception {
		EzySetterMethod set = new EzySetterMethod(
				ClassA.class.getDeclaredMethod("set", String.class));
		assert set.getFieldName().equals("set");
	}
	
	public static class ClassA {
		
		public void setA(String a) {
			
		}
		
		public void setValue(String value) {
		}
		
		public void duplicateValue(String value) {
			
		}
		
		public void set(String a) {
			
		}
		
	}
	
}
