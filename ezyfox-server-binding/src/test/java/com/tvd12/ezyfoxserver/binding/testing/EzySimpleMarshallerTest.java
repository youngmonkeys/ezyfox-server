package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleMarshaller;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

public class EzySimpleMarshallerTest extends BaseTest {

	@Test
	public void test1() {
		EzySimpleMarshaller marshaller = new EzySimpleMarshaller();
		marshaller.addWriter(ClassA.class, new EzyObjectWriterBuilder(new EzyClass(ClassA.class)).build());
		ClassA[] arrayInput = new ClassA[] {new ClassA(), new ClassA(), new ClassA()};
		EzyArray arrayOutput = marshaller.marshal(arrayInput);
		assert arrayOutput.size() == 3;
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzySimpleMarshaller marshaller = new EzySimpleMarshaller();
		marshaller.marshal(new Object());
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test3() {
		EzySimpleMarshaller marshaller = new EzySimpleMarshaller();
		marshaller.marshal(ExWriter.class, new Object());
	}
		
	public static class ClassA {
		
	}
	
	@SuppressWarnings("rawtypes")
	public static class ExWriter implements EzyWriter {

		@Override
		public Object write(EzyMarshaller marshaller, Object object) {
			return null;
		}
		
	}
}
