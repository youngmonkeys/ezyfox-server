package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccount;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.impl.TestAccountService;

public class AccountService2Test extends HazelcastBaseTest {

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test() {
		TestAccountService service = new TestAccountService();
		service.setHazelcastInstance(HZ_INSTANCE);
		service.setMaxIdService(MAX_ID_SERVICE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.setAccountFactory(new TestAccountFactory());
		EzyAccount account = service.newAccount("MONEY");
		service.update(account.getId(), 100);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		TestAccountService service = new TestAccountService(HZ_INSTANCE);
		service.setMaxIdService(MAX_ID_SERVICE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.setAccountFactory(new TestAccountFactory());
		EzyAccount account = service.newAccount("MONEY");
		service.update(account.getId(), 0.5D);
	}
	
}
