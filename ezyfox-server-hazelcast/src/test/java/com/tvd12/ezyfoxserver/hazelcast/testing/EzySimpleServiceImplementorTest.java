package com.tvd12.ezyfoxserver.hazelcast.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezyfoxserver.hazelcast.impl.EzySimpleServiceImplementor;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

public class EzySimpleServiceImplementorTest extends HazelcastBaseTest {

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test() {
		EzySimpleServiceImplementor implementor = 
				new EzySimpleServiceImplementor(new EzyClass(ClassA.class));
		implementor.implement(HZ_INSTANCE);
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test2() {
		EzySimpleServiceImplementor implementor = 
				new EzySimpleServiceImplementor(new EzyClass(InterfaceA.class));
		implementor.implement(null);
	}
	
	@EzyMapServiceAutoImpl("a")
	public static class ClassA {
		
	}
	
	@EzyMapServiceAutoImpl("a")
	public static interface InterfaceA {
		
	}
	
}
