package com.tvd12.ezyfoxserver.database.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.database.exception.EzyTransactionException;

public class EzyTransactionExceptionTest {

	@Test
	public void test() {
		new EzyTransactionException();
		new EzyTransactionException("");
		new EzyTransactionException("", new Throwable());
	}
	
}
