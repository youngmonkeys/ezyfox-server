package com.tvd12.ezyfoxserver.testing.function;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyCastToByte;
import com.tvd12.test.base.BaseTest;

public class EzyCastIntToByteTest extends BaseTest {

	@Test
	public void test() {
		EzyCastToByte cast = new EzyCastToByte() {};
		assert cast.cast(100) == (byte)100;
		assert cast.cast(100L) == (byte)100;
	}
	
}
