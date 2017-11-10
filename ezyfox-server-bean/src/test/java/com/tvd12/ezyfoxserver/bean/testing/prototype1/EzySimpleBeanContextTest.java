package com.tvd12.ezyfoxserver.bean.testing.prototype1;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.impl.EzySimpleBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.impl.EzyByConstructorPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.util.EzyMapBuilder;

public class EzySimpleBeanContextTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Exception {
		EzyByConstructorPrototypeSupplierLoader.setDebug(true);
		
		EzyBeanContext context = EzySimpleBeanContext.builder()
				.scan("com.tvd12.ezyfoxserver.bean.testing.prototype1")
				.build();
		List<Object> prototypes = context.getPrototypes(EzyMapBuilder.mapBuilder()
				.put("type", "request_listener")
				.put("cmd", "2")
				.build());
		System.out.println(prototypes);
		assert prototypes.size() == 2;
		
		prototypes = context.getPrototypes(EzyMapBuilder.mapBuilder()
				.put("type", "request_listener")
				.put("cmd", "1")
				.build());
		System.out.println(prototypes);
		assert prototypes.size() == 1;
		
		Object prototype = context.getPrototype(EzyMapBuilder.mapBuilder()
				.put("type", "request_listener")
				.put("cmd", "1")
				.build());
		assert prototype != null;
		
		prototype = context.getPrototype(EzyMapBuilder.mapBuilder()
				.put("type", "request_listenerzz")
				.put("cmd", "1zz")
				.build());
		assert prototype == null;
	}
	
}
