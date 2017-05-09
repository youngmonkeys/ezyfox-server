package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAbstractAccount;
import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccount;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyAbstractAccoutFactory;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.impl.TestAccountService;

public class AccountServiceTest extends HazelcastBaseTest {

	@Test
	public void test() {
		TestAccountService service = new TestAccountService(HZ_INSTANCE);
		service.setMaxIdService(MAX_ID_SERVICE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.setAccountFactory(new EzyAbstractAccoutFactory() {
			@Override
			protected EzyAbstractAccount newAccount() {
				return new TestAcount();
			}
		});
		EzyAccount account = service.newAccount("MONEY");
		assert account.getValue() == 0;
		assert service.update(account.getId(), 100) == 100L;
		assertEquals(service.update(account.getId(), -0.3D), new Long[] {-30L, 70L, 100L});
		assert service.getAccount(account.getId()).getValue() == 70L;
		EzyAccount account2 = service.newAccount("MONEY");
		assert service.getAccounts(Sets.newHashSet(account.getId(), account2.getId())).size() == 2;
		EzyAccount account2r = service.removeAccount(account2.getId());
		assert account2.equals(account2r);
		assert account2.hashCode() == account2r.hashCode();
		service.removeAccounts(Sets.newHashSet(account.getId(), account2.getId()));
		assert service.getAccount(account.getId()) == null;
	}
	
}
