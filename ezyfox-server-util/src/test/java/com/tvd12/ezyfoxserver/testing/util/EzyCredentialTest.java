package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyCredential;
import com.tvd12.test.base.BaseTest;

public class EzyCredentialTest extends BaseTest {

	@Test
	public void test() {
		EzyCredential credential = new EzyCredential();
		credential.setUsername("dungtv");
		credential.setPassword("123456");
		assert credential.getUsername().equals("dungtv");
		assert credential.getPassword().equals("123456");
	}
	
}
