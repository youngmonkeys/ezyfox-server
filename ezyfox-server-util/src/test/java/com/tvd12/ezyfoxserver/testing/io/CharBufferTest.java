package com.tvd12.ezyfoxserver.testing.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.testng.annotations.Test;

public class CharBufferTest {

	@Test
	public void test() {
		try {
			String hello = "Hello World";
			ByteBuffer buffer = ByteBuffer.allocate(hello.length());
			CharBuffer charBuffer = buffer.asCharBuffer();
			charBuffer.put(hello);
			charBuffer.flip();
			byte[] bytes = new byte[hello.length()];
			buffer.get(bytes);
			System.out.println(new String(bytes));
		}
		catch(Exception e) {
		}
	}
	
}
