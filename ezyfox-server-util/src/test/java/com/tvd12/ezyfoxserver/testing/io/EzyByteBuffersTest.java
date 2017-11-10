package com.tvd12.ezyfoxserver.testing.io;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyByteBuffers;
import com.tvd12.test.base.BaseTest;

public class EzyByteBuffersTest extends BaseTest {

	@Test
	public void test() {
		ByteBuffer buffer = ByteBuffer.allocate(8).putLong(1000L);
		buffer.flip();
		EzyByteBuffers.getBytes(buffer);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyByteBuffers.class;
	}
	
}
