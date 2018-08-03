package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionOptions.TransactionType;

public class GetForUpdatePerfomanceTest extends HazelcastBaseTest {
	
	public static void main(String[] args) throws Exception {
		new GetForUpdatePerfomanceTest().test();
	}
	
	public void testX() throws Exception {
		HZ_INSTANCE.getMap("map").clear();
		AtomicInteger done = new AtomicInteger(0);
		TransactionOptions txOptions = newHazelcastTransactionOptions();
		Runnable runnable  = () ->  {
			TransactionContext txCxt = HZ_INSTANCE.newTransactionContext(txOptions);
			txCxt.beginTransaction();
			try {
			TransactionalMap<String, Long> map = txCxt.getMap("map");
			for(int i = 0 ; i < 50 ; i++) {
				String key = "" + i;
				Long oldValue = map.getForUpdate(key);
				map.set(key, oldValue != null ? oldValue + 1L : 1L);
				Long newValue = map.get(key);
				if(newValue == null) {
					System.out.println("hello");	
				}
			}
			txCxt.commitTransaction();
			}
			catch (Exception e) {
				txCxt.rollbackTransaction();
			}
			finally {
				done.incrementAndGet();
			}
		};
		Thread[] threads = new Thread[1000];
		for(int i = 0 ; i < threads.length ; i++)
			threads[i] = new Thread(runnable);
		for(int i = 0 ; i < threads.length ; i++)
			threads[i].start();
		while (done.get() < threads.length);
		Map<String, Long> xmap = HZ_INSTANCE.getMap("map");
		for(String key : xmap.keySet())
			System.out.println(key + " : " + xmap.get(key));
		
	}

	public void test() {
		HZ_INSTANCE.getMap("map").clear();
		TransactionOptions txOptions = newHazelcastTransactionOptions();
		TransactionContext txCxt = HZ_INSTANCE.newTransactionContext(txOptions);
		txCxt.beginTransaction();
		TransactionalMap<Object, Object> map = txCxt.getMap("map");
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < 50000 ; i++) {
			map.getForUpdate("" + i);
			map.set("" + i, i);
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
