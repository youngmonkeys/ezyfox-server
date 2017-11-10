package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Field;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyFieldFinder;
import com.tvd12.test.base.BaseTest;

public class EzyFieldFinderTest extends BaseTest {
	
	@Test
	public void test() {
		Field a = EzyFieldFinder.builder()
				.clazz(B.class)
				.fieldName("a")
				.build()
				.find();
		assertNotNull(a);
		
		Field b = EzyFieldFinder.builder()
				.clazz(B.class)
				.fieldName("b")
				.build()
				.find();
		assertNotNull(b);
		
		Field c = EzyFieldFinder.builder()
				.clazz(B.class)
				.fieldName("c")
				.build()
				.find();
		assertNotNull(c);
		
		Field d = EzyFieldFinder.builder()
				.clazz(B.class)
				.fieldName("d")
				.build()
				.find();
		assertNull(d);
	}

	public static interface I  {
		String a = "";
	}
	
	public abstract class A implements I {
		String b;
	}
	
	public abstract class B extends A {
		String c;
	}
	
}
