package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleMarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleUnmarshaller;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.test.performance.Performance;

public class EzyPointWriterExample7 {

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		EzyWriter<Point, EzyObject> pointWriter = new EzyPointWriterImpl();
		EzyWriter<EzyTestData, EzyObject> dataWriter = new EzyTestDataWriterImpl();
		EzyWriter<EzyTestData1, EzyArray> data1Writer = new TestData1WriterImpl();
		
		EzySimpleMarshaller marshaller = new EzySimpleMarshaller();
		marshaller.addWriter(Point.class, pointWriter);
		marshaller.addWriter(EzyTestData.class, dataWriter);
		marshaller.addWriter(data1Writer);
		
		EzyObject outObject = pointWriter.write(marshaller, new Point());
		
		System.out.println(outObject);
		
		EzyReader<EzyObject, Point> pointReader = new EzyPointReaderImpl();
		EzyReader<EzyObject, EzyTestData> dataReader = new EzyTestDataReaderImpl();
		EzyReader<EzyArray, EzyTestData1> data1Reader = new TestData1ReaderImpl();
		
		EzySimpleUnmarshaller unmarshaller = new EzySimpleUnmarshaller();
		unmarshaller.addReader(Point.class, pointReader);
		unmarshaller.addReader(EzyTestData.class, dataReader);
		unmarshaller.addReader(data1Reader);
		
		Point outPoint = pointReader.read(unmarshaller, outObject);
		System.out.println(outPoint);
		
		long time1 = Performance.create()
				.loop(10000)
				.test(() -> 
					pointWriter.write(marshaller, new Point())
				)
				.getTime();
			
		System.out.println(time1);
		
		long time2 = Performance.create()
				.loop(10000)
				.test(() -> 
					pointWriter.write(marshaller, new Point())
				)
				.getTime();
			
		System.out.println(time2);
		
		long time3 = Performance.create()
			.loop(10000)
			.test(() -> 
				pointReader.read(unmarshaller, outObject)
			)
			.getTime();
		
		System.out.println(time3);
		
		long time4 = Performance.create()
				.loop(10000)
				.test(() -> 
					pointReader.read(unmarshaller, outObject)
				)
				.getTime();
			
		System.out.println(time4);
		
	}
}
