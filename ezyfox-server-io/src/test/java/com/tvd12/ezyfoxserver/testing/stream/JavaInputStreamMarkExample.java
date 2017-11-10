package com.tvd12.ezyfoxserver.testing.stream;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class JavaInputStreamMarkExample {

	public static void main(String[] args) throws IOException {
		InputStream stream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
		assert stream.markSupported();
		assert stream.available() == 10;
		byte[] bytes = new byte[2];
		stream.read(bytes);
		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
		assertEquals(stream.available(), 13);
		stream.read(bytes);
		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
		assertEquals(stream.available(), 11);
		stream.read(bytes);
		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
		assertEquals(stream.available(), 9);
		stream.mark(3);
		System.err.println("========== after set mark =========");
		System.err.println("available: " + stream.available());
		stream.read(bytes);
		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
		stream.reset();
		System.err.println("========== after reset =========");
		System.err.println("available: " + stream.available());
		stream.read(bytes);
		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
	}
	
}
