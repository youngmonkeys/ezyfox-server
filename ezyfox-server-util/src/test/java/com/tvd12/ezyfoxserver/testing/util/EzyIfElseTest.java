package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyIfElse;
import com.tvd12.test.base.BaseTest;
import static org.testng.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

public class EzyIfElseTest extends BaseTest {

	@Test
	public void test() {
		AtomicInteger num = new AtomicInteger(0);
		EzyIfElse.withIf(true, () -> num.incrementAndGet());
		assertEquals(num.get(), 1);
		EzyIfElse.withIf(false, () -> num.incrementAndGet());
		assertEquals(num.get(), 1);
		EzyIfElse.withElse(false, () -> num.incrementAndGet());
		assertEquals(num.get(), 2);
		EzyIfElse.withElse(true, () -> num.incrementAndGet());
		assertEquals(num.get(), 2);
		EzyIfElse.withIfElse(true, () -> num.incrementAndGet(), () -> num.incrementAndGet());
		assertEquals(num.get(), 3);
		EzyIfElse.withIfElse(false, () -> num.incrementAndGet(), () -> num.incrementAndGet());
		assertEquals(num.get(), 4);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyIfElse.class;
	}
	
}
