package com.tvd12.ezyfoxserver.testing.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.util.EzyHashMapList;
import com.tvd12.ezyfoxserver.util.EzyMapList;
import com.tvd12.test.base.BaseTest;

public class EzyHashMapListTest extends BaseTest {

	@Test
	public void test() {
		EzyMapList<String, String> map = new EzyHashMapList<>();
		map.addItems("1", "a", "b", "c");
		assertEquals(map.get("1"), Lists.newArrayList("a", "b", "c"));
		map.removeItems("1", "b", "c");
		assertEquals(map.get("1"), Lists.newArrayList("a"));
		map.removeItems("zzz");
		assert map.getItems("abc").size() == 0;
		map.addItems("a", Lists.newArrayList("a", "b", "c"));
		assert map.getItems("a").size() == 3;
		map.deepClear();
	}
	
}
