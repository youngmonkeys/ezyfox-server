package com.tvd12.ezyfoxserver.testing.stream;

import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.stream.EzyInputStreamReader;
import com.tvd12.ezyfoxserver.stream.EzySimpleInputStreamReader;
import com.tvd12.test.base.BaseTest;

public class EzySimpleInputStreamReaderTest extends BaseTest {

	@Test
	public void test() {
		EzyInputStreamReader reader = EzySimpleInputStreamReader.builder()
				.build();
		reader.readBytes(new InputStream() {
			int index = 0;
			private byte[] bytes = new byte[] {1, 2, 3};
			@Override
			public int read() throws IOException {
				if(index >= bytes.length)
					return -1;
				return bytes[index ++];
			}
		});
	}
	
	@Test
	public void test1() {
		EzyInputStreamReader reader = EzySimpleInputStreamReader.builder()
				.build();
		reader.readString(new InputStream() {
			int index = 0;
			private byte[] bytes = new byte[] {1, 2, 3};
			@Override
			public int read() throws IOException {
				if(index >= bytes.length)
					return -1;
				return bytes[index ++];
			}
		}, "UTF-8");
	}
	
	@Test
	public void test2() {
		EzyInputStreamReader reader = EzySimpleInputStreamReader.builder()
				.build();
		reader.readChars(new InputStream() {
			int index = 0;
			private byte[] bytes = new byte[] {1, 2, 3};
			@Override
			public int read() throws IOException {
				if(index >= bytes.length)
					return -1;
				return bytes[index ++];
			}
		}, "UTF-8");
	}
	
}
