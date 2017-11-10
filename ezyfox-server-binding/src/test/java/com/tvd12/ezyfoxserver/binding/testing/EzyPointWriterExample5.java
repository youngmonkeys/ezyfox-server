package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;

public class EzyPointWriterExample5 {

	@Test
	public void test() throws Exception {
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addClass(Point.class)
				.addClass(EzyTestData.class)
				.addTemplate(new TestData1WriterImpl())
				.build();
		
		EzyMarshaller marshaller = context.newMarshaller();
		
		System.out.println(marshaller.marshal(new Point()).toString());
	}
}
