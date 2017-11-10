package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.TransactionalMap;
import com.tvd12.ezyfoxserver.function.EzyExceptionFunction;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleMaxIdService;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyTransactionOptions;

public class EzySimpleMaxIdServiceTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		final EzySimpleMaxIdService service = new EzySimpleMaxIdService(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		
		List<Long> nums = new ArrayList<>();
		Thread[] threads = new Thread[1000];
		for(int i = 0 ; i < threads.length ; i++) {
			threads[i] = new Thread(() -> {
				nums.add(service.incrementAndGet("something"));
			});
		}
		for(int i = 0 ; i < threads.length ; i++) {
			threads[i].start();
			threads[i].join();
		}
		
		System.out.println(nums);
		for(int i = 0 ; i < nums.size() - 1 ; i++) {
			if(nums.get(i + 1) != nums.get(i) + 1) {
				System.err.println("error in " + i);
			}
		}
		
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test2() {
		EzySimpleMaxIdService service = new EzySimpleMaxIdService();
		service.setHazelcastInstance(HZ_INSTANCE);
		service.setMapTransactionFactory(new EzyMapTransactionFactory() {
			
			@Override
			public <K, V, R> EzyMapReturnTransaction<K, V, R> newReturnTransaction(String mapName,
					EzyTransactionOptions options) {
				return new EzyMapReturnTransaction<K, V, R>() {
					@Override
					public R apply(EzyExceptionFunction<TransactionalMap<K, V>, R> func) throws Exception {
						throw new RuntimeException();
					}

					@Override
					public void begin() {
					}

					@Override
					public void commit() {
					}

					@Override
					public void rollback() {
					}
				};
			}
			
			@Override
			public <K, V> EzyMapApplyTransaction<K, V> newApplyTransaction(String mapName, EzyTransactionOptions options) {
				return null;
			}
		});
		service.incrementAndGet("d");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test3() {
		IMap map = mock(IMap.class);
		HazelcastInstance hzInstance = mock(HazelcastInstance.class);
		when(hzInstance.getMap(anyString())).thenReturn(map);
		EzySimpleMaxIdService service = new EzySimpleMaxIdService(hzInstance);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.loadAll();
	}
	
}
