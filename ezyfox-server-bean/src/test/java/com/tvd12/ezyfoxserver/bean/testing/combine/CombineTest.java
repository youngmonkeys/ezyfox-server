package com.tvd12.ezyfoxserver.bean.testing.combine;

import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactory;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfoxserver.bean.EzySingletonFactory;
import com.tvd12.ezyfoxserver.bean.impl.EzyByConstructorPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.bean.testing.combine.pack1.ClassA1;
import com.tvd12.ezyfoxserver.bean.testing.combine.pack1.Singleton1;

public class CombineTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		EzyByConstructorPrototypeSupplierLoader.setDebug(true);
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.scan(
						"com.tvd12.ezyfoxserver.bean.testing.combine.pack0",
						"com.tvd12.ezyfoxserver.bean.testing.combine.pack1",
						"com.tvd12.ezyfoxserver.bean.testing.combine.pack2"
				)
				.addProperty("hello", "world")
				.addProperty("foo", "bar")
				.addProperty("array", "1,2,3,4,5")
				.addProperty("ints", Lists.newArrayList(1, 2, 3))
				.addSingletonClasses(new Class[] {
						SingletonX1.class, 
						SingletonX2.class,
						Singleton12.class
				})
				.addPrototypeClasses(new Class[] {
						ClassA12.class
				})
				.addSingleton("singleton2", new SingletonX3());
		EzyBeanContext context = builder.build();
		SingletonX1 x1 = (SingletonX1) context.getBean("singletonX1", SingletonX1.class);
		SingletonX2 x2 = (SingletonX2) context.getBean("singletonX2", SingletonX2.class);
		
		assert x1.getSingletonX1() == x1;
		assert x1.getSingletonX2() == x2;
		assert x2.getSingletonX1() == x1;
		assert x2.getSingletonX2() == x2;
		
		Singleton1 singleton1 = (Singleton1) context.getBean("s1", Singleton1.class);
		assert singleton1 != null;
		assert singleton1.getMap() != null;
		
		ClassA12 a121 = (ClassA12) context.getBean("classA12", ClassA12.class);
		ClassA12 a122 = (ClassA12) context.getBean("classA12", ClassA12.class);
		assert a121 != a122;

		EzySingletonFactory singletonFactory = context.getSingletonFactory();
		EzyPrototypeFactory prototypeFactory = context.getPrototypeFactory();
		
		EzyPrototypeSupplier a1Supplier = prototypeFactory.getSupplier("a1", ClassA1.class);
		prototypeFactory.getProperties(a1Supplier);
		
		Object s1Singleton = singletonFactory.getSingleton("s1", Object.class);
		singletonFactory.getProperties(s1Singleton);
		
		List<Object> singletons = singletonFactory.getSingletons(EzyCombine0Ann.class);
		assert singletons.size() == 2;
		
		List<EzyPrototypeSupplier> prototypeSuppliers = prototypeFactory.getSuppliers(EzyCombine0Ann.class);
		assert prototypeSuppliers.size() == 2;
	}
	
}
