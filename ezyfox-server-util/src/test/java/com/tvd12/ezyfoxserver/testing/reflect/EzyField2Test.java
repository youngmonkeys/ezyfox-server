package com.tvd12.ezyfoxserver.testing.reflect;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.test.base.BaseTest;

public class EzyField2Test extends BaseTest {

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() throws Exception {
		EzyField field = new EzyField(ClassA.class.getDeclaredField("a"));
		field.set(new ClassA(), "10");
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() throws Exception {
		EzyField field = new EzyField(ClassA.class.getDeclaredField("a"));
		field.get(this);
	}
	
	@Test
	public void test3() throws Exception {
		EzyField field = new EzyField(ClassA.class.getDeclaredField("b"));
		ClassA obj = new ClassA();
		assert field.get(obj).equals("b");
		field.set(obj, "10");
		assert field.get(obj).equals("10");
	}
	
	public static class ClassA {
		protected final String a = "a";
		public String b = "b";
	}
	
}
