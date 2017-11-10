package com.tvd12.ezyfoxserver.bean.testing;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.impl.EzyByMethodSingletonLoader;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.test.base.BaseTest;

public class EzyByMethodSingletonLoaderTest extends BaseTest {

	public A newA() {
		return new A();
	}
	
	public B newB() {
		return new B();
	}
	
	@Test
	public void test() throws Exception {
		EzyBeanContext context = EzyBeanContext.builder()
				.build();
		EzyMethod methodA = new EzyMethod(getClass().getDeclaredMethod("newA"));
		EzyMethod methodB = new EzyMethod(getClass().getDeclaredMethod("newB"));
		EzyByMethodSingletonLoader loader = new EzyByMethodSingletonLoader(
				methodA, this, EzyMaps.newHashMap(B.class, methodB));
		loader.load(context);
		
		Method getConstructorParameterTypes = EzyByMethodSingletonLoader.class
				.getDeclaredMethod("getConstructorParameterTypes", Class.class);
		getConstructorParameterTypes.setAccessible(true);
		getConstructorParameterTypes.invoke(loader, B.class);
		getConstructorParameterTypes.invoke(loader, Object.class);
	}
	
	public static class A {
		@EzyAutoBind
		public B b;
	}
	
	public static class B {
		
	}
	
}
