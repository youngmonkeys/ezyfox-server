package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.io.EzyCollections;
import com.tvd12.test.base.BaseTest;

public class EzyCollectionsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyCollections.class;
	}
	
	@Test
	public void test() {
		assertTrue(EzyCollections.containsAny(
				Sets.newHashSet("1", "2", "3"), Sets.newHashSet("2", "4", "5")));
		assertEquals(EzyCollections.countItems(
				Sets.newHashSet("ab", "ac", "de", "ef", "ah"), str -> str.startsWith("a")), 3);
		assertEquals(EzyCollections.flatMapToInt(
				Sets.newHashSet('a', 'b', 'c'), ch -> (int)ch), (int)'a' + (int)'b' + (int)'c');
		Set<String> set = Sets.newHashSet("a", "b", "c");
		assertEquals(EzyCollections.getItem(set, (i) -> i.equals("b")), "b");
		assertNull(EzyCollections.getItem(set, (i) -> i.equals("z")));
		assertEquals(EzyCollections.toArray(Lists.newArrayList("a", "b", "c"), String[]::new), 
				new String[] {"a", "b", "c"});
	}
	
	@Test
	public void test1() {
		assert EzyCollections.isEmpty(null);
		assert EzyCollections.isEmpty(Lists.newArrayList());
		assert !EzyCollections.isEmpty(Lists.newArrayList(1));
	}
	
}
