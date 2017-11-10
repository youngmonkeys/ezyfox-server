package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyInts;
import com.tvd12.test.base.BaseTest;

public class EzyIntsTest extends BaseTest {

	@Test
	public void test() {
		assertEquals(EzyInts.bin2int(31), Integer.MAX_VALUE);
		assertEquals(EzyInts.bin2int(
				EzyBytes.getBytes(ByteBuffer.allocate(4).putInt(-100))), -100);
		assertEquals(EzyInts.bin2uint(
				EzyBytes.getBytes(ByteBuffer.allocate(4).putInt(100))), 100);
		ByteBuffer buffer1 = ByteBuffer.allocate(4).putInt(-1000);
		buffer1.flip();
		assertEquals(EzyInts.bin2int(buffer1), -1000);
		
		ByteBuffer buffer2 = ByteBuffer.allocate(4).putInt(1000);
		buffer2.flip();
		assertEquals(EzyInts.bin2uint(buffer2), 1000);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyInts.class;
	}
	
}
