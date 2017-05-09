package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.util.EzySum;
import com.tvd12.test.base.BaseTest;

public class EzySumTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzySum.class;
	}
	
	@Test
	public void test() {
		assert EzySum.sumBytes(new byte[] {1, 2, 3}) == 6;
		assert EzySum.sumBytes(Lists.newArrayList((byte)1, (byte)2, (byte)3)) == 6;
		
		assert EzySum.sumDoubles(new double[] {1D, 2D, 3D}) == 6D;
		assert EzySum.sumDoubles(Lists.newArrayList(1D, 2D, 3D)) == 6D;
		
		assert EzySum.sumFloats(new float[]{1F, 2F, 3F}) == 6D;
		assert EzySum.sumFloats(Lists.newArrayList(1F, 2F, 3F)) == 6F;
		
		assert EzySum.sumInts(new int[]{1, 2, 3}) == 6D;
		assert EzySum.sumInts(Lists.newArrayList(1, 2, 3)) == 6;
		
		assert EzySum.sumLongs(new long[]{1, 2, 3}) == 6D;
		assert EzySum.sumLongs(Lists.newArrayList(1L, 2L, 3L)) == 6L;
		
		assert EzySum.sumShorts(new short[]{1, 2, 3}) == 6D;
		assert EzySum.sumShorts(Lists.newArrayList((short)1, (short)2, (short)3)) == 6;
		
		assert EzySum.sumToInt(new String[] {"1", "2", "3"}, s -> Integer.valueOf(s)) == 6;
		assert EzySum.sumToInt(Lists.newArrayList("1", "2", "3"), s -> Integer.valueOf(s)) == 6;
		
		assert EzySum.sumToLong(new String[] {"1", "2", "3"}, s -> Long.valueOf(s)) == 6;
		assert EzySum.sumToLong(Lists.newArrayList("1", "2", "3"), s -> Long.valueOf(s)) == 6;
	}
	
}
