package com.tvd12.ezyfoxserver.testing.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.test.base.BaseTest;

public class EzyProcessorTest extends BaseTest {

	@Test
	public void test() {
		EzyProcessor.processWithException(() -> {
		});
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test1() {
		EzyProcessor.processWithException(() -> {
			throw new Exception();
		});
	}
	
	@Test
	public void test2() {
		EzyProcessor.processWithIllegalArgumentException(() -> {
		});
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test4() {
		EzyProcessor.processWithIllegalArgumentException(() -> {
			throw new Exception();
		});
	}
	
	
	@Test
	public void test5() {
		Lock lock = new ReentrantLock();
		EzyProcessor.processWithLock(() -> {}, lock);
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test6() {
		Lock lock = new ReentrantLock();
		EzyProcessor.processWithLock(() -> {throw new IllegalStateException();}, lock);
	}
	
	@Test
	public void test7() {
		Lock lock = new ReentrantLock();
		try {
			EzyProcessor.processWithTryLock(() -> {}, lock, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test8() {
		Lock lock = new ReentrantLock();
		try {
			EzyProcessor.processWithTryLock(() -> {throw new IllegalStateException();}, lock, 10);
		} catch (InterruptedException e) {
		}
	}
	
	@Test
	public void test9() {
		Lock lock = new ReentrantLock() {
			private static final long serialVersionUID = 616957346213118575L;

			public boolean tryLock(long time, TimeUnit timeUnit) throws InterruptedException {
				throw new InterruptedException();
			};
		};
		try {
			EzyProcessor.processWithTryLock(() -> {
			}, lock, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test10() {
		Lock lock = new ReentrantLock() {
			private static final long serialVersionUID = 616957346213118575L;

			public boolean tryLock(long time, TimeUnit timeUnit) throws InterruptedException {
				return false;
			};
		};
		try {
			EzyProcessor.processWithTryLock(() -> {
			}, lock, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test11() {
		Lock lock = new ReentrantLock() {
			private static final long serialVersionUID = 616957346213118575L;

			public boolean tryLock()  {
				return false;
			};
		};
		EzyProcessor.processWithTryLock(() -> {
		}, lock);
	}
	
	@Test
	public void test12() {
		Lock lock = new ReentrantLock();
		EzyProcessor.processWithTryLock(() -> {
		}, lock);
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test13() {
		Lock lock = new ReentrantLock();
		EzyProcessor.processWithTryLock(() -> {
			throw new IllegalStateException();
		}, lock);
	}
	
	@Test
	public void test14() {
		EzyProcessor.processWithLogException(() -> {});
	}
	
	@Test
	public void test15() {
		EzyProcessor.processWithLogException(() -> {throw new Exception();});
	}
	
	@Test
	public void test16() {
		EzyProcessor.processWithSync(() -> {}, this);
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test17() {
		EzyProcessor.processWithSync(() -> {throw new IllegalStateException();}, this);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyProcessor.class;
	}
	
}
