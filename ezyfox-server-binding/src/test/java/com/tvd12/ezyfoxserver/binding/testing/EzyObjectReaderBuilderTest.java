package com.tvd12.ezyfoxserver.binding.testing;

import java.util.Collection;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.impl.EzyObjectReaderBuilder;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

public class EzyObjectReaderBuilderTest extends BaseTest {

	@Test
	public void test() {
		EzyObjectReaderBuilder.setDebug(true);
		EzyObjectReaderBuilder builder = new EzyObjectReaderBuilder(new EzyClass(ClassA.class));
		builder.build();
	}

	@SuppressWarnings("rawtypes")
	public static class ClassA {
		public Map map;
		public void setCollection(Collection collection) {
		}
	}
	
}
