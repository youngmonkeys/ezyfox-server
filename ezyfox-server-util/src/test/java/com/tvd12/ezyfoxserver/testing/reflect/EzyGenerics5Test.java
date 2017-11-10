package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

public class EzyGenerics5Test {

	public static void main(String[] args) throws Exception {
		Field field = ClassA.class.getDeclaredField("doc");
		System.out.println(field.getGenericType());
		System.out.println(field.getGenericType() instanceof ParameterizedType);
	}
	
	public static class ClassA {
		public Doc doc;
	}
	
	public static class Doc extends HashMap<String, String> {
		private static final long serialVersionUID = 7094427792585792941L;
		
	}
	
}
