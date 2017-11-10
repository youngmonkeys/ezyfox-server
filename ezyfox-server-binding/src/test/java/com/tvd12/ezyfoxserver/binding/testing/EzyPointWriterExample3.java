package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleMarshaller;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

public class EzyPointWriterExample3 {

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		EzyClass pointClazz = new EzyClass(Point.class);
		EzyClass dataClazz = new EzyClass(EzyTestData.class);
		EzyObjectWriterBuilder pointWriterBuilder = new EzyObjectWriterBuilder(pointClazz);
		EzyObjectWriterBuilder dataWriterBuilder = new EzyObjectWriterBuilder(dataClazz);
		EzyWriter<Object, Point> pointWriter = pointWriterBuilder.build();
		EzyWriter<Object, Point> dataWriter = dataWriterBuilder.build();
		
		EzySimpleMarshaller marshaller = new EzySimpleMarshaller();
		marshaller.addWriter(Point.class, pointWriter);
		marshaller.addWriter(EzyTestData.class, dataWriter);
		marshaller.addWriter(new TestData1WriterImpl());
		
		System.out.println(pointWriter.write(marshaller, new Point()));
		
//		long time1 = Performance.create()
//			.loop(1000000)
//			.test(() -> 
//				writer.write(new Point())
//			)
//			.getTime();
//		
//		System.out.println(time1);
//		
//		ExWriter wr = new ExWriter();
//		long time2 = Performance.create()
//				.loop(1000000)
//				.test(() -> 
//				wr.write(new Point())
//				)
//				.getTime();
//			
//		System.out.println(time2);
//		
//		long time3 = Performance.create()
//				.loop(1000000)
//				.test(() -> {
//					Point point = new Point();
//					EzyObjectBuilder ebuilder = EzyEntityFactory.create(EzyObjectBuilder.class);
//					ebuilder.append("x", point.getX());
//					ebuilder.append("x", point.getY());
//					ebuilder.append("xY", point.getXY());
//					ebuilder.build();
//				})
//				.getTime();
//			
//		System.out.println(time3);
			
		
	}
}
