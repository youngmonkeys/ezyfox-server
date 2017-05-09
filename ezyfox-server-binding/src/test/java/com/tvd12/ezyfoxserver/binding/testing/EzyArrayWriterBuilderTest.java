package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReader;
import com.tvd12.ezyfoxserver.binding.annotation.EzyWriter;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayWriterBuilder;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2Object;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2ObjectReader;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2ObjectWriter;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

public class EzyArrayWriterBuilderTest extends BaseTest {

	@Test
	public void test() {
		EzyArrayWriterBuilder builder = new EzyArrayWriterBuilder(new EzyClass(ClassA.class));
		builder.build();
	}
	
	@Test
	public void test2() {
		EzyArrayWriterBuilder.setDebug(true);
		EzyArrayWriterBuilder builder = new EzyArrayWriterBuilder(new EzyClass(ClassB.class));
		builder.build();
	}
	
	public static class ClassA {
		
		@EzyReader(Scan2ObjectReader.class)
		@EzyWriter(Scan2ObjectWriter.class)
		public Scan2Object object = new Scan2Object();
		
		protected String value1 = "value1";
		
		protected void setValue1(String value1) {
			this.value1 = value1;
		}
		
	}
	
	@EzyArrayBinding
	public static class ClassB {
		
		private String value1 = "value1";
		protected String value2 = "value2";
		protected String value3 = "value3";
		
		protected void setValue1(String value1) {
			this.value1 = value1;
		}
		
		protected String getValue1() {
			return value1;
		}
		
		public String getValue3x() {
			return "";
		}
		
	}
	
}
