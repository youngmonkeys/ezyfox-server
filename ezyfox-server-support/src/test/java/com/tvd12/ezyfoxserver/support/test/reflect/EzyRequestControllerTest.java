package com.tvd12.ezyfoxserver.support.test.reflect;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController;

public class EzyRequestControllerTest {

	@Test
	public void test() {
		EzyRequestControllerProxy controllerProxy = 
				new EzyRequestControllerProxy(new HelloController());
		System.out.println(controllerProxy);
	}
	
}
