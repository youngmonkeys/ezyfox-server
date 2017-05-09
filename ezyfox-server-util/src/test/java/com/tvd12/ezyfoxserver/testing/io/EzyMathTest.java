package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyMath;
import com.tvd12.test.base.BaseTest;
import static org.testng.Assert.*;

import java.nio.ByteBuffer;

public class EzyMathTest extends BaseTest {

	@Test
	public void test() {
		assertEquals(EzyMath.bin2uint(
				new byte[] {1, 2, 3, 4}), 
				ByteBuffer.wrap(new byte[] {1, 2, 3, 4}).getInt());
		assertEquals(EzyMath.bin2int(8), 255);
		
		assertEquals(EzyMath.bin2ulong(
				new byte[] {1, 2, 3, 4, 5, 6, 7, 8}), 
				ByteBuffer.wrap(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}).getLong());
		assertEquals(EzyMath.bin2long(40), 1099511627775L);
		
		assertEquals(EzyMath.bin2int(
				getBytes(ByteBuffer.allocate(4).putInt(-100))), 
				-100);
		
		assertEquals(EzyMath.bin2long(
				getBytes(ByteBuffer.allocate(8).putLong(-100000))), 
				-100000L);
		byte[] xor = new byte[] {(byte) 255, (byte) 255, (byte) 255};
		EzyMath.xor(xor);
		assertEquals(xor, new byte[] {0, 0, 0});
	}
	
	protected byte[] getBytes(ByteBuffer buffer) {
		return EzyBytes.getBytes(buffer);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyMath.class;
	}
	
}
