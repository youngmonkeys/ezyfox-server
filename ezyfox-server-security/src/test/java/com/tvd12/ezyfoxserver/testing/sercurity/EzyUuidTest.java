package com.tvd12.ezyfoxserver.testing.sercurity;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.sercurity.EzyUuid;
import com.tvd12.test.base.BaseTest;

public class EzyUuidTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyUuid.class;
	}
	
	@Test
	public void test() {
		assert !EzyUuid.random().equals(EzyUuid.random());
	}
	
}
