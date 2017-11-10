package com.tvd12.ezyfoxserver.bean.testing.prototype;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfoxserver.bean.impl.EzyPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.bean.impl.EzySimpleBeanContext;
import com.tvd12.ezyfoxserver.bean.impl.EzySimplePrototypeFactory;
import com.tvd12.ezyfoxserver.bean.impl.EzyByConstructorPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

public class GeneratedClassAFactory2 {

	@Test
	public void test() throws Exception {
		EzyByConstructorPrototypeSupplierLoader.setDebug(true);
		
		EzyPrototypeSupplierLoader builder = 
				new EzyByConstructorPrototypeSupplierLoader(new EzyClass(ClassA.class));
		
		EzyPrototypeSupplier supplier = builder.load(new EzySimplePrototypeFactory());
		
		EzyBeanContext context = EzySimpleBeanContext.builder()
				.scan("com.tvd12.ezyfoxserver.bean.testing.prototype")
				.build();
		System.out.println(supplier.getObjectType());
		ClassA classA = (ClassA) supplier.supply(context);
		System.out.println(classA.getClassB());
		classA = (ClassA) supplier.supply(context);
		System.out.println(classA.getClassB());
	}
	
}
