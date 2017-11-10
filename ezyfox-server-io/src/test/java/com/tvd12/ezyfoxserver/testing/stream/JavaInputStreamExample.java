package com.tvd12.ezyfoxserver.testing.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class JavaInputStreamExample {

	public static void main(String[] args) throws IOException {
		InputStream stream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
		assert stream.markSupported();
		assert stream.available() == 10;
		byte[] bytes = new byte[2];
		stream.read(bytes);
		assert stream.available() == 8;
		assert stream.skip(2) == 2L;
		stream.mark(8);
		assert stream.available() == 6;
		stream.read(bytes);
		assert stream.available() == 4;
		stream.read(bytes);
		assert stream.available() == 2;
		stream.read(bytes);
		assert stream.available() == 0;
		stream.reset();
		assert stream.available() == 10;
		System.err.println(Arrays.toString(bytes));
		stream.mark(2);
		stream.read(bytes, 0, 2);
		System.err.println(Arrays.toString(bytes));
	}
	
}
