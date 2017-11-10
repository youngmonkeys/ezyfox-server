package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.test.base.BaseTest;

public class EzyPackagesTest extends BaseTest {

	public java.lang.Class<?> getTestClass() {
		return EzyPackages.class;
	};
	
	@Test
	public void test() {
		assertTrue(EzyPackages.getAnnotatedClasses(
				"com.tvd12.ezyfoxserver.testing.reflect", ExampleAnnotation.class)
				.contains(ClassA.class));
	}
	
	@Test
	public void test1() {
		Set<Class<?>> set = EzyPackages.getExtendsClasses(
				"com.tvd12.ezyfoxserver.testing.reflect", InterfaceA.class);
		System.out.println(set);
		
		set = EzyPackages.getExtendsClasses(
				"com.tvd12.ezyfoxserver.testing.reflect", ClassA.class);
		System.out.println(set);
		
	}
	
	@ExampleAnnotation
	public static class ClassA {
		
	}
	
	public static class ClassB extends ClassA {
		
	}
	
	public static interface InterfaceA {
	}
	
	public static interface InterfaceB extends InterfaceA {
		
	}
}
