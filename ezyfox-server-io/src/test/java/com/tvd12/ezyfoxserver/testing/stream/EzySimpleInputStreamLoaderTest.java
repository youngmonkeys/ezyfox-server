package com.tvd12.ezyfoxserver.testing.stream;

import java.io.FileNotFoundException;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyFileNotFoundException;
import com.tvd12.ezyfoxserver.stream.EzySimpleInputStreamLoader;
import com.tvd12.test.base.BaseTest;

public class EzySimpleInputStreamLoaderTest extends BaseTest {

	@Test(expectedExceptions = {EzyFileNotFoundException.class})
	public void test1() {
		EzySimpleInputStreamLoader.builder()
			.throwException(true)
			.build()
			.load("ffsadfs.afasdf");
	}
	
	@Test(expectedExceptions = {EzyFileNotFoundException.class})
	public void test2() {
		new EzySimpleInputStreamLoader.Builder() {
			public com.tvd12.ezyfoxserver.stream.EzyInputStreamLoader build() {
				return new EzySimpleInputStreamLoader(this) {
					protected java.io.File getFile(String filePath) throws FileNotFoundException {
						throw new FileNotFoundException();
					};
				};
			};
		}
			.throwException(true)
			.build()
			.load("ffsadfs.afasdf");
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test3() {
		new EzySimpleInputStreamLoader.Builder() {
			public com.tvd12.ezyfoxserver.stream.EzyInputStreamLoader build() {
				return new EzySimpleInputStreamLoader(this) {
					protected java.io.File getFile(String filePath) throws FileNotFoundException {
						throw new NullPointerException();
					};
				};
			};
		}
			.throwException(true)
			.build()
			.load("ffsadfs.afasdf");
	}
	
	public void test4() {
		assert new EzySimpleInputStreamLoader.Builder() {
			public com.tvd12.ezyfoxserver.stream.EzyInputStreamLoader build() {
				return new EzySimpleInputStreamLoader(this) {
					protected java.io.File getFile(String filePath) throws FileNotFoundException {
						throw new NullPointerException();
					};
				};
			};
		}
			.throwException(false)
			.build()
			.load("ffsadfs.afasdf") == null;
	}
	
}
