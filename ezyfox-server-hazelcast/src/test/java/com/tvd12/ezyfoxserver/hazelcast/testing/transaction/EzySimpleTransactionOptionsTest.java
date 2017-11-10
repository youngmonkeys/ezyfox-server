package com.tvd12.ezyfoxserver.hazelcast.testing.transaction;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyTransactionOptions;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyTransactionType;
import com.tvd12.test.base.BaseTest;

public class EzySimpleTransactionOptionsTest extends BaseTest {

	@Test
	public void test() {
		EzyTransactionOptions options = EzyTransactionOptions.newInstance()
				.timeout(1000L, TimeUnit.MILLISECONDS)
				.durability(2)
				.transactionType(EzyTransactionType.ONE_PHASE);
		assert options.getTimeout() == 1000L;
		assert options.getTimeoutUnit() == TimeUnit.MILLISECONDS;
		assert options.getDurability() == 2;
		assert options.getTransactionType() == EzyTransactionType.ONE_PHASE;
	}
	
}
