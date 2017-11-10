package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyArrays;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.test.base.BaseTest;

public class EzyBytesTest extends BaseTest {

	@Test
	public void test() {
		assertEquals(EzyBytes.copy(ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6}), 3), 
				new byte[] {1, 2, 3});
		assertEquals(EzyBytes.getBytes(1, 255 + 1, 2), 
				new byte[] {1, 1, 0});
		assertEquals(EzyBytes.getBytes((byte)1, 100.5D), 
				EzyArrays.merge((byte)1, EzyBytes.getBytes(ByteBuffer.allocate(8).putDouble(100.5D))));
		assertEquals(EzyBytes.getBytes((byte)1, 100.5F), 
				EzyArrays.merge((byte)1, EzyBytes.getBytes(ByteBuffer.allocate(4).putFloat(100.5F))));
		assertEquals(EzyBytes.getBytes(100L), 
				EzyBytes.getBytes(ByteBuffer.allocate(8).putLong(100L)));
		assertEquals(EzyBytes.getBytes(100), 
				EzyBytes.getBytes(ByteBuffer.allocate(4).putInt(100)));
		assertEquals(EzyBytes.getBytes((short)100), 
				EzyBytes.getBytes(ByteBuffer.allocate(2).putShort((short)100)));
		assertEquals(EzyBytes.getBytes((byte)1, new byte[] {2, 3}), 
				new byte[] {1, 2, 3});
		
	}
	
	@Test
	public void test1() {
		assert EzyBytes.totalBytes(new byte[][] {{1,2},{1,2,3}}) == 5;
		assertEquals(EzyBytes.copy(new byte[][] {{1,2},{1,2,3}}), new byte[] {1,2,1,2,3}); 
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyBytes.class;
	}
	
}
