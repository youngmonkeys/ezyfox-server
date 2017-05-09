package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleInputTransformer;
import com.tvd12.ezyfoxserver.testing.entity.EzyEntityTest;
import static org.testng.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EzySimpleInputTransformerTest extends EzyEntityTest {

	private EzyInputTransformer transformer = new EzySimpleInputTransformer();
	
	@Test
	public void test() {
		assertEquals(transform(new boolean[] {true, false, true}), 
				Lists.newArrayList(true, false, true));
		
		assertEquals(transform(new double[] {1D, 2D, 3D}), 
				Lists.newArrayList(1D, 2D, 3D));
		
		assertEquals(transform(new float[] {1F, 2F, 3F}), 
				Lists.newArrayList(1F, 2F, 3F));
		
		assertEquals(transform(new int[] {1, 2, 3}), 
				Lists.newArrayList(1, 2, 3));
		
		assertEquals(transform(new long[] {1L, 2L, 3L}), 
				Lists.newArrayList(1L, 2L, 3L));
		
		assertEquals(transform(new short[] {(short)1, (short)2, (short)3}), 
				Lists.newArrayList((short)1, (short)2, (short)3));
		
		assertEquals(transform(new String[] {"a", "b", "c"}), 
				Lists.newArrayList("a", "b", "c"));
		
		
		assertEquals(transform(new Boolean[] {true, false, true}), 
				Lists.newArrayList(true, false, true));
		
		assertEquals(transform(new Byte[] {1, 2, 3}), 
				new byte[] {1, 2, 3});
		
		assertEquals(transform(new Character[] {'a', 'b', 'c'}), 
				new byte[] {'a', 'b', 'c'});
		
		assertEquals(transform(new Double[] {1D, 2D, 3D}), 
				Lists.newArrayList(1D, 2D, 3D));
		
		assertEquals(transform(new Float[] {1F, 2F, 3F}), 
				Lists.newArrayList(1F, 2F, 3F));
		
		assertEquals(transform(new Integer[] {1, 2, 3}), 
				Lists.newArrayList(1, 2, 3));
		
		assertEquals(transform(new Long[] {1L, 2L, 3L}), 
				Lists.newArrayList(1L, 2L, 3L));
		
		assertEquals(transform(new Short[] {(short)1, (short)2, (short)3}), 
				Lists.newArrayList((short)1, (short)2, (short)3));
		
		assertEquals(transform(new String[] {"a", "b", "c"}), 
				Lists.newArrayList("a", "b", "c"));
		
		assertEquals(transform(String.class), String.class.getName());
		
		EzyObject[] objects = new EzyObject[] {
				newObjectBuilder().append("1", "a").build()
		};
		assertEquals(((EzyArray)transform(objects)).toList(), 
				Lists.newArrayList(objects));
		
		assertEquals(transform(LocalDate.of(2017, 05, 30)), "2017-05-30");
		
		assertEquals(transform(LocalDateTime.of(2017, 05, 30, 12, 34, 56, 78)), "2017-05-30T12:34:56:000");
		
		transformer.transform(newArrayBuilder());
	}
	
	private Object transform(Object value) {
		return transformer.transform(value);
	}
	
}
