package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionOptions.TransactionType;

public class GetForUpdatePerfomanceTest extends HazelcastBaseTest {
	
	public static void main(String[] args) {
		new GetForUpdatePerfomanceTest().test();
	}

	public void test() {
		TransactionOptions txOptions = newHazelcastTransactionOptions();
		TransactionContext txCxt = HZ_INSTANCE.newTransactionContext(txOptions);
		txCxt.beginTransaction();
		TransactionalMap<Object, Object> map = txCxt.getMap("map");
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < 50000 ; i++) {
			map.getForUpdate("" + i);
		}
		txCxt.commitTransaction();
		long offset = System.currentTimeMillis() - start;
		System.out.println(offset);
	}
	
	private TransactionOptions newHazelcastTransactionOptions() {
		return new TransactionOptions()
				.setTimeout(300, TimeUnit.SECONDS)
				.setTransactionType(TransactionType.TWO_PHASE)
				.setDurability(300);
	}
	
}
