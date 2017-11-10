package com.tvd12.ezyfoxserver.hazelcast.testing.entity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyNegativeValueException;
import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAbstractAccount;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractAccountTest extends BaseTest {

	@Test
	public void test1() {
		MyAccount account = new MyAccount();
		account.setAcceptNegativeValue(true);
		account.update(100);
	}
	
	@Test
	public void test2() {
		MyAccount account = new MyAccount();
		account.setAcceptNegativeValue(true);
		account.update(-100);
	}
	
	@Test
	public void test3() {
		MyAccount account = new MyAccount();
		account.setValue(100L);
		account.setAcceptNegativeValue(true);
		Long[] update = account.update(-0.5D, 200L);
		assertEquals(update, new long[] {-50L, 250L, 100});
	}
	
	@Test
	public void test4() {
		MyAccount account = new MyAccount();
		account.setValue(100L);
		account.setAcceptNegativeValue(true);
		assert account.isAcceptNegativeValue();
		Long[] update = account.update(-1.5D, 0L);
		assertEquals(update, new long[] {-150L, -50, 100});
	}
	
	@Test(expectedExceptions = {EzyNegativeValueException.class})
	public void test5() {
		MyAccount account = new MyAccount();
		account.setValue(100L);
		Long[] update = account.update(-1.5D, 0L);
		assertEquals(update, new long[] {-150L, -50, 100});
	}
	
	public static class MyAccount extends EzyAbstractAccount {
		private static final long serialVersionUID = -640884788284000277L;

		@Override
		public Long getId() {
			return 1L;
		}

		@Override
		public void setId(Long id) {
		}
		
	}
	
}
