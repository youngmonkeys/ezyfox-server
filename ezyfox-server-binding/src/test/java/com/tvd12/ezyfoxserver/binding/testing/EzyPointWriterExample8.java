package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.entity.EzyObject;

public class EzyPointWriterExample8 {

	@Test
	public void test() throws Exception {
		
		EzyBindingContext context = EzyBindingContext.builder()
				.addClass(Point.class)
				.addClass(EzyTestData.class)
				.addTemplate(new TestData1WriterImpl())
				.addTemplate(new TestData1ReaderImpl())
				.build();
		
		EzyMarshaller marshaller = context.newMarshaller();
		EzyObject outObject = marshaller.marshal(new Point());
		System.out.println(outObject);
		
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		Point outPoint = unmarshaller.unmarshal(outObject, Point.class);
		System.out.println(outPoint);
	}
}
