package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyGenerics;
import com.tvd12.test.base.BaseTest;

public class EzyGenericsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyGenerics.class;
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() {
		EzyGenerics.getGenericClassArguments(getGenericType("a"), 1);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzyGenerics.getGenericClassArguments(getGenericType("b"), 2);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test3() {
		EzyGenerics.getGenericClassArguments(getGenericType("e"), 2);
	}
	
	@Test
	public void test4() {
		EzyGenerics.getTwoGenericClassArguments(getGenericType("c"));
	}
	
	private Type getGenericType(String fieldName) {
		return field(fieldName).getGenericType();
	}
	
	private Field field(String field) {
		try {
			return ClassA.class.getDeclaredField(field);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static class ClassA {
		String a;
		Collection<String> b;
		Map<String, String> c;
		Map<Collection<String>, Map<String, String>> d;
		Map<?, ?> e;
	}
	
}
