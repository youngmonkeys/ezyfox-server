package com.tvd12.ezyfoxserver.bean.testing;

import java.util.Arrays;

public class SuperAndInterfaceTest {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(B.class.getInterfaces()));
	}
	
	public static interface A {
		
	}
	
	public static interface B extends A {
		
	}
	
}
