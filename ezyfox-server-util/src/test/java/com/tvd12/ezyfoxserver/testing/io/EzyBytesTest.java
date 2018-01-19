package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class EzyBytesTest extends BaseTest {

	@Test
	public void test() {
		assertEquals(EzyBytes.copy(ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6}), 3), 
				new byte[] {1, 2, 3});
		assertEquals(EzyBytes.getBytes(1, 255 + 1, 2), 
				new byte[] {1, 1, 0});
		assertEquals(EzyBytes.getBytes((byte)1, 100.5D), 
				EzyBytes.merge((byte)1, EzyBytes.getBytes(ByteBuffer.allocate(8).putDouble(100.5D))));
		assertEquals(EzyBytes.getBytes((byte)1, 100.5F), 
				EzyBytes.merge((byte)1, EzyBytes.getBytes(ByteBuffer.allocate(4).putFloat(100.5F))));
		assertEquals(EzyBytes.getBytes(100L), 
				EzyBytes.getBytes(ByteBuffer.allocate(8).putLong(100L)));
		assertEquals(EzyBytes.getBytes(100), 
				EzyBytes.getBytes(ByteBuffer.allocate(4).putInt(100)));
		assertEquals(EzyBytes.getBytes((short)100), 
				EzyBytes.getBytes(ByteBuffer.allocate(2).putShort((short)100)));
	}
	
	@Test
	public void test1() {
		assert EzyBytes.totalBytes(new byte[][] {{1,2},{1,2,3}}) == 5;
		assertEquals(EzyBytes.merge(new byte[][] {{1,2},{1,2,3}}), new byte[] {1,2,1,2,3}); 
	}
	
	@Test
	public void mergePerformaceTest() {
		byte[][] bytess = new byte[5][100];
		for(int i = 0 ; i < bytess.length ; i++) {
			for(int k = 0 ; k < bytess[i].length ; k++) {
				bytess[i][k] = 1;
			}
		}
		long time1 = Performance.create()
				.test(() -> {
					EzyBytes.merge(bytess);
				})
				.getTime();
		System.out.println("mergePerformaceTest.time1 = " + time1);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyBytes.class;
	}
	
}
