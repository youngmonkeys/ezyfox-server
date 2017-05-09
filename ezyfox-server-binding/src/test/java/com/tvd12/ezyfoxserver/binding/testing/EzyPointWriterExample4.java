package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleMarshaller;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyPointWriterExample4 {

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		EzyWriter<Object, Point> pointWriter = new EzyPointWriterImpl();
		EzyWriter<Object, Point> dataWriter = new EzyTestDataWriterImpl();
		EzyWriter<EzyTestData1, EzyArray> data1Writer = new TestData1WriterImpl();
		
		EzySimpleMarshaller marshaller = new EzySimpleMarshaller();
		marshaller.addWriter(Point.class, pointWriter);
		marshaller.addWriter(EzyTestData.class, dataWriter);
		marshaller.addWriter(data1Writer);
		
		System.out.println(pointWriter.write(marshaller, new Point()));
		
	}
}
