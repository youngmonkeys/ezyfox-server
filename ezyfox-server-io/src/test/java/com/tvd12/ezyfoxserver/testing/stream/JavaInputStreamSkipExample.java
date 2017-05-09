package com.tvd12.ezyfoxserver.testing.stream;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaInputStreamSkipExample {

	public static void main(String[] args) throws IOException {
		InputStream stream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
		assert stream.markSupported();
		assert stream.available() == 10;
		byte[] bytes = new byte[2];
		stream.read(bytes);
		System.err.println("before skip");
		System.err.println("available: " + stream.available());
		stream.skip(2);
		System.err.println("after skip");
		System.err.println("available: " + stream.available());
		System.err.println("before reset");
		stream.reset();
		System.err.println("after reset");
		System.err.println("available: " + stream.available());
		assertEquals(stream.available(), 10);
	}
	
}
