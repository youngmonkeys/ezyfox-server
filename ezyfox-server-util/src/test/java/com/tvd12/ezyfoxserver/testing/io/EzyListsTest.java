package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.test.base.BaseTest;

public class EzyListsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyLists.class;
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		List<String> set = EzyLists.combine(Sets.newHashSet("1", "2", "3"),
				Sets.newHashSet("4", "5", "6"));
		assertEquals(set, Lists.newArrayList("1", "2", "3", "4", "5", "6"));
		
		Collection<String> coll1 = Lists.newArrayList("ab", "cde");
		List<Character> set1 = EzyLists.newHashSetByAddAll(coll1, this::stringtoChars);
		assertEquals(set1, Lists.newArrayList('a', 'b', 'c', 'd', 'e'));
		
		List<String> set2 = EzyLists.filter(set, 
				(str) -> (!str.startsWith("1") && !str.startsWith("5")));
		assertEquals(set2, Lists.newArrayList("2", "3", "4", "6"));
		
		List<String> set3 = EzyLists.newArrayList(set, "3" ,"4");
		assertEquals(set3, Lists.newArrayList("1", "2", "5", "6"));
		
		List<String> set4 = EzyLists.newArrayList(new Long[] {1L, 2L, 3L}, v -> v.toString());
		assertEquals(set4, Lists.newArrayList("1", "2", "3"));
		
		List<String> set5 = EzyLists.newArrayList(Lists.newArrayList(1L, 2L, 3L), v -> v.toString());
		assertEquals(set5, Lists.newArrayList("1", "2", "3"));
		
		Map<String, Long> map = new HashMap<>();
		map.put("a", 1L);
		map.put("b", 2L);
		List<String> set6 = EzyLists.newArrayList(map, (k, v) -> v + k);
		assertEquals(set6, Lists.newArrayList("1a", "2b"));
		
		List<String> set7 = EzyLists.addElementsToNewList(new ArrayList<>(), "1", "2", "3");
		assertEquals(set7, Lists.newArrayList("1", "2", "3"));
		
		List<String> list8 = Lists.newArrayList("1", "2", "3");
		EzyLists.resize(list8, 10, "def");
		assert list8.size() == 10;
		System.out.println(list8);
		EzyLists.resize(list8, 5, "def");
		System.out.println(list8);
		assert list8.size() == 5;
		EzyLists.resize(list8, 5, "def");
		assert list8.size() == 5;
	}
	
	private Collection<Character> stringtoChars(String str) {
		List<Character> answer = new ArrayList<>();
		for(int i = 0 ; i < str.length() ; i++)
			answer.add(str.charAt(i));
		return answer;
	}
}
