package com.tvd12.ezyfoxserver.testing.annotation;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.test.base.BaseTest;

public class Implementer extends BaseTest {

	public static void main(String[] args) {
		System.out.println(PersonMapService.class.isAnnotationPresent(EzyMapServiceAutoImpl.class));
		System.out.println(PersonMapService.class.isAnnotationPresent(EzyAutoImpl.class));
	}
	
}
