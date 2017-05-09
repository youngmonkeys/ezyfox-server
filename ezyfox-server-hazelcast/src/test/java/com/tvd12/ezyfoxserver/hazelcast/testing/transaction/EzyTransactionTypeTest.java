package com.tvd12.ezyfoxserver.hazelcast.testing.transaction;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyTransactionType;
import com.tvd12.test.base.BaseTest;

public class EzyTransactionTypeTest extends BaseTest {

	@Test
	public void test() {
		assert EzyTransactionType.TWO_PHASE.getId() == 1;
		assert EzyTransactionType.TWO_PHASE.getName().equals("TWO_PHASE");
		assert EzyTransactionType.valueOf(1) == EzyTransactionType.TWO_PHASE;
		EzyTransactionType.values();
		assert EzyTransactionType.valueOf("TWO_PHASE") == EzyTransactionType.TWO_PHASE;
	}
	
}
