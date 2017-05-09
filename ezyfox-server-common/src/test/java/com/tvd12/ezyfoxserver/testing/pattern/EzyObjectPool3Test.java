package com.tvd12.ezyfoxserver.testing.pattern;

import org.testng.annotations.Test;

import com.tvd12.test.base.BaseTest;

public class EzyObjectPool3Test extends BaseTest {

	@Test
	public void test() throws Exception {
		MyTestObjectPool3 pool = MyTestObjectPool3.builder()
				.maxObjects(3)
				.minObjects(1)
				.validationInterval(30)
				.build();
		pool.start();
		MyTestObject o1 = pool.borrowOne();
		Thread.sleep(31);
		MyTestObject o2 = pool.borrowOne();
		Thread.sleep(31);
		MyTestObject o3 = pool.borrowOne();
		Thread.sleep(31);
		
		pool.returnOne(o1);
		Thread.sleep(31);
		pool.returnOne(o2);
		Thread.sleep(31);
		pool.returnOne(o3);
		
		Thread.sleep(2 * 1000);
		pool.destroy();
	}
	
}
