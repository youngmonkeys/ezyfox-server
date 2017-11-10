package com.tvd12.ezyfoxserver.bean.testing;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.bean.EzyBeanNameTranslator;
import com.tvd12.ezyfoxserver.bean.testing.combine.ClassA12;
import com.tvd12.ezyfoxserver.bean.testing.combine.Singleton12;
import com.tvd12.ezyfoxserver.bean.testing.combine.SingletonX1;
import com.tvd12.ezyfoxserver.bean.testing.combine.SingletonX2;
import com.tvd12.ezyfoxserver.bean.testing.combine.SingletonX3;
import com.tvd12.ezyfoxserver.properties.EzySimplePropertiesReader;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.test.base.BaseTest;

public class EzyBeanNameTranslatorTest extends BaseTest {

	@Test
	public void test() {
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.scan(
						"com.tvd12.ezyfoxserver.bean.testing.combine"
				)
				.addProperties(new HashMap<>())
				.propertiesReader(new EzySimplePropertiesReader())
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
		EzyBeanNameTranslator translator = context.getBeanNameTranslator();
		translator.map("map", ConcurrentHashMap.class, EzyClasses.getVariableName(ConcurrentHashMap.class));
		Object concurrentHashMap = context.getBean(ConcurrentHashMap.class);
		assert concurrentHashMap != null;
		Object map = context.getBean("map", ConcurrentHashMap.class);
		assert map != null;
		assert map instanceof ConcurrentHashMap;
	}
	
}
