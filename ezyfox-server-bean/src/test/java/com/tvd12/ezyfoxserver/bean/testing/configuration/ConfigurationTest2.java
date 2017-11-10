package com.tvd12.ezyfoxserver.bean.testing.configuration;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfoxserver.bean.impl.EzySimpleConfigurationLoader;
import com.tvd12.test.base.BaseTest;

public class ConfigurationTest2 extends BaseTest {

	@Test
	public void test() {
		EzyBeanContext context = EzyBeanContext.builder().build();
		context.getSingletonFactory().addSingleton(new Singleton1());
		context.getPrototypeFactory().addSupplier(new EzyPrototypeSupplier() {
			
			@Override
			public Object supply(EzyBeanContext context) {
				return new PrototypeEx11();
			}
			
			@Override
			public Class<?> getObjectType() {
				return PrototypeEx11.class;
			}
		});
		
		context.getPrototypeFactory().addSupplier(new EzyPrototypeSupplier() {
			
			@Override
			public Object supply(EzyBeanContext context) {
				return new PrototypeEx12();
			}
			
			@Override
			public Class<?> getObjectType() {
				return PrototypeEx12.class;
			}
		});
		
		new EzySimpleConfigurationLoader()
				.clazz(ConfigClassA2.class)
				.context(context)
				.load();
		
		
		
	}
	
}
