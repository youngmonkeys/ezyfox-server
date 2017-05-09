package com.tvd12.ezyfoxserver.binding.testing.scan1;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.entity.EzyObject;

public class Scan1Example1 {

	public static void main(String[] args) throws Exception {
		new Scan1Example1().test();
	}
	
	@Test
	public void test() {
		EzyObjectReaderBuilder.setDebug(true);
		EzyArrayReaderBuilder.setDebug(true);
		EzyObjectWriterBuilder.setDebug(true);
		EzyArrayWriterBuilder.setDebug(true);
		
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.scan1")
				.addTemplate(new Scan1ClassCWriterImpl())
				.addTemplate(new Scan1ClassCReaderImpl())
				.build();
		
		EzyMarshaller marshaller = context.newMarshaller();
		EzyObject outObject = marshaller.marshal(new Scan1ClassA());
		System.out.println(outObject);
		outObject.put("hung", "xau trai");
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		Scan1ClassA outEntity = unmarshaller.unmarshal(outObject, Scan1ClassA.class);
		System.out.println(outEntity);
	}
}
