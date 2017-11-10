package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ClassTypeTest {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		System.out.println(ClassTypeTest.class instanceof Type);
		
		Map<String, String> map = new HashMap<>();
		Class clazz = map.getClass();
		System.out.println((Type)clazz instanceof ParameterizedType);
	}
	
}
