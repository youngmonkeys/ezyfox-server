package com.tvd12.ezyfoxserver.hazelcast.testing.entity;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyMongoAccount;
import com.tvd12.test.base.BaseTest;

public class EzyMongoAccountTest extends BaseTest {

	@Test
	public void test() {
		EzyMongoAccount account = new EzyMongoAccount();
		account.setId(1L);
		assert account.getId() == 1L;
	}
	
}
