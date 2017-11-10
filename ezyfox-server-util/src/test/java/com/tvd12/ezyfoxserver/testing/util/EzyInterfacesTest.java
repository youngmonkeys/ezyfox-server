package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.function.EzyApply;
import com.tvd12.ezyfoxserver.reflect.EzyInterfaces;
import com.tvd12.test.base.BaseTest;

public class EzyInterfacesTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyInterfaces.class;
	}
	
	@Test
	public void test() {
		assert EzyInterfaces.getInterface(InterfaceB.class, InterfaceC.class) != null;
		assert EzyInterfaces.getInterface(InterfaceB.class, EzyApply.class) == null;
	}
	
	public static class ClassA {
	}
	
	public static interface InterfaceA<I,E> {
	}
	
	public static interface InterfaceB extends InterfaceA<String, ClassA>, InterfaceC<String, ClassA> {
		
	}
	
	public static interface InterfaceC<I,E> {
	}
}
