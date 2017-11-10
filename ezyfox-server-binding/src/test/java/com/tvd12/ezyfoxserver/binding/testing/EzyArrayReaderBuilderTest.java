package com.tvd12.ezyfoxserver.binding.testing;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.annotation.EzyReader;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayReaderBuilder;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2Object;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2ObjectReader;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

public class EzyArrayReaderBuilderTest extends BaseTest {

	@Test
	public void test() {
		EzyArrayReaderBuilder builder 
			= new EzyArrayReaderBuilder(new EzyClass(ClassA.class));
		builder.build();
	}

	@SuppressWarnings("rawtypes")
	public static class ClassA {
		public Map map = new HashMap<>();
		@EzyReader(Scan2ObjectReader.class)
		public Scan2Object object = new Scan2Object();
		public ClassB classB;
		
		public void setValue(String value) {
			
		}
	}
	
	@SuppressWarnings({ "rawtypes", "serial" })
	public static class ClassB extends HashMap {
		
	}
	
}
