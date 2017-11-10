package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.test.base.BaseTest;

public class EzyMethodTest extends BaseTest {

	@Test
	public void test() throws Exception {
		EzyMethod method1 = EzyMethod.builder()
				.clazz(ClassA.class)
				.methodName("getValue")
				.parameterTypes(String.class, String.class)
				.build();
		method1.setDisplayName("getValue$impl");
		assertEquals(method1.getDisplayName(), "getValue$impl");
		assertEquals(method1.getReturnType(), String.class);
		assertEquals(method1.getGenericReturnType(), String.class);
		assertEquals(method1.getGenericParameterTypes().length, 2);
		assertEquals(method1.getParameterTypes().length, 2);
		assertFalse(method1.isAnnotated(ExampleAnnotation.class));
		assertNull(method1.getAnnotation(ExampleAnnotation.class));
		method1.getDeclaration();
		method1.getPublicDeclaration();
		method1.toString();
		
		EzyMethod method2 = new EzyMethod(ClassA.class.getDeclaredMethod("notSetter1", String.class));
		assertFalse(method2.isSetter());
		assertFalse(method2.isGetter());
		EzyMethod method3 = new EzyMethod(ClassA.class.getDeclaredMethod("notSetter2"));
		assertFalse(method3.isSetter());
		assertFalse(method3.isGetter());
		EzyMethod method4 = new EzyMethod(ClassA.class.getDeclaredMethod("notSetter3", String.class, String.class));
		assertFalse(method4.isSetter());
		assertFalse(method4.isGetter());
		EzyMethod method5 = new EzyMethod(ClassA.class.getDeclaredMethod("setNotSetter", String.class, String.class));
		assertFalse(method5.isSetter());
		assertFalse(method5.isGetter());
		
		EzyMethod method6 = new EzyMethod(ClassA.class.getDeclaredMethod("getNotGetter3", String.class));
		assertFalse(method6.isSetter());
		assertFalse(method6.isGetter());
		
		EzyMethod method7 = new EzyMethod(ClassA.class.getDeclaredMethod("getNotGetter4"));
		assertFalse(method7.isSetter());
		assertFalse(method7.isGetter());
		
		assert !method7.equals(method6);
		method7.hashCode();
		
		EzyMethod method8 = new EzyMethod(ClassA.class.getDeclaredMethod("newHello"));
		assert method8.getFieldName().equals("hello");
	}
	
	public static class ClassA {
		
		public String getValue(String a, String b) {
			return "value";
		}
		
		public String newHello() {
			return "hello";
		}
		
		public void notSetter1(String a) {
		}
		
		protected void notSetter2() {
		}
		
		protected void notSetter3(String a, String b) {
		}
		
		public void setNotSetter(String a, String b) {
			
		}
		
		public String getNotGetter3(String a) {
			return null;
		}
		
		public void getNotGetter4() {
		}
		
	}
	
}
