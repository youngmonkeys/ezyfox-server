package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleOutputTransformer;
import com.tvd12.ezyfoxserver.testing.entity.EzyEntityTest;

public class EzySimpleOutputTransformerTest extends EzyEntityTest {

	private EzyOutputTransformer transfomer 
			= new EzySimpleOutputTransformer();
	
	@Test
	public void test() {
		ClassA classA = new ClassA();
		EzyArray array = newArray();
		assertNull(transfomer.transform(null, String.class));
		assertEquals(transfomer.transform(classA, ClassA.class), classA);
		assertEquals(transfomer.transform(String.class.getName(), Class.class), String.class);
		assertEquals((((EzyObject[])transfomer.transform(array, EzyObject[].class))).length, 1);
		
		assertNull(transfomer.transform("124", Date.class));
		assertNull(transfomer.transform("124", Class.class));
		
		assertNull(transfomer.transform("124", LocalDate.class));
		assertNull(transfomer.transform("124", LocalDateTime.class));
		
		assertEquals(transfomer.transform("2017-05-30", LocalDate.class), 
				LocalDate.of(2017, 05, 30));
		assertEquals(transfomer.transform("2017-05-30T12:34:56:000", LocalDateTime.class), 
				LocalDateTime.of(2017, 05, 30, 12, 34, 56, 0));
	}
	
	private EzyArray newArray() {
		EzyObjectBuilder objectBuilder = EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("1", "a");
		return EzyEntityFactory.create(EzyArrayBuilder.class).append(objectBuilder).build();
	}
	
	public static class ClassA {
		
	}
	
}
