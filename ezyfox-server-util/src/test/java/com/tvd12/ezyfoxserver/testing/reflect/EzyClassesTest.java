package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.Constructor;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.test.base.BaseTest;

public class EzyClassesTest extends BaseTest {

	@Test
	public void test() {
		EzyClasses.newInstance(String.class.getName());
		EzyClasses.newInstance(String.class.getName(), getClass().getClassLoader());
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() {
		EzyClasses.newInstance("aaa");
		EzyClasses.newInstance("aaa", getClass().getClassLoader());
	}
	
	@Test
	public void test2() throws Exception {
		Constructor<ClassA> constructor = EzyClasses.getConstructor(ClassA.class);
		EzyClasses.newInstance(constructor);
		EzyClasses.newInstance(ClassA.class);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyClasses.class;
	}
	
	public static class ClassA {
	}
	
}
