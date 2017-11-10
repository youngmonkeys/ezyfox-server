package com.tvd12.ezyfoxserver.hazelcast.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.hazelcast.impl.EzySimpleMapNameFetcher;

public class EzySimpleMapNameFetcherTest {

	@Test
	public void test1() {
		try {
			new EzySimpleMapNameFetcher().getMapName(Object.class);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		try {
			new EzySimpleMapNameFetcher().getMapName(InterfaceA.class);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void test3() {
		try {
			new EzySimpleMapNameFetcher().getMapName(InterfaceB.class);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	@EzyAutoImpl(properties = {
	})
	public static interface InterfaceA {
		
	}
	
	@EzyAutoImpl(properties = {
			@EzyKeyValue(key = "key", value = "value")
	})
	public static interface InterfaceB {
		
	}
	
}
