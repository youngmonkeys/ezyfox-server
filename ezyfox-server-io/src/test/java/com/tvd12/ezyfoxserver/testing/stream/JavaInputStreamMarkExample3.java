package com.tvd12.ezyfoxserver.testing.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class JavaInputStreamMarkExample3 {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		InputStream stream = new FileInputStream(new File("src/test/resources/example.txt"));
		assert stream.markSupported();
		assert stream.available() == 10;
		byte[] bytes = new byte[3];
		stream.read(bytes);
		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
//		assertEquals(stream.available(), 13);
		stream.mark(2);
		System.err.println("========== after set mark =========");
		stream.reset();
//		System.err.println("========== after reset =========");
//		stream.read(bytes);
//		System.err.println("bytes: " + Arrays.toString(bytes));
		System.err.println("available: " + stream.available());
		
	}
	
}
