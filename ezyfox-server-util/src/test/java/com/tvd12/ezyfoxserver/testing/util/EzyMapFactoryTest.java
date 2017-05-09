package com.tvd12.ezyfoxserver.testing.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyMapFactory;
import com.tvd12.test.base.BaseTest;

public class EzyMapFactoryTest extends BaseTest {

	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
	public void test() {
		EzyMapFactory factory = new EzyMapFactory();
		Map map = factory.newMap(Map.class);
		HashMap hashMap = factory.newMap(HashMap.class);
		TreeMap treeMap = factory.newMap(TreeMap.class);
		ConcurrentHashMap concurrentHashMap = factory.newMap(ConcurrentHashMap.class);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() {
		EzyMapFactory factory = new EzyMapFactory();
		factory.newMap(Integer.class);
	}
}
