package com.tvd12.ezyfoxserver.bean.testing.combine2;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.bean.impl.EzyByConstructorPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.bean.impl.EzySimpleErrorHandler;

public class Combine2Test {

	@Test
	public void test() throws Exception {
		EzyByConstructorPrototypeSupplierLoader.setDebug(true);
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.scan(
						"com.tvd12.ezyfoxserver.bean.testing.combine2"
				)
				.errorHandler(new EzySimpleErrorHandler());
		EzyBeanContext context = builder.build();
		context.getPrototypeFactory();
		
	}
	
}
