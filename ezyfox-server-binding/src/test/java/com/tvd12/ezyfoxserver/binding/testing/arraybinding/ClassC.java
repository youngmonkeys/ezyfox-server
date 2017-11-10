package com.tvd12.ezyfoxserver.binding.testing.arraybinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EzyArrayBinding(indexes = {"map1", "list1", "map2", "list2", "longs1", "classAs1"})
@ToString
public class ClassC {
	private Map<String, String> map1 = newMap1();
	private List<String> list1 = Lists.newArrayList("a", "b", "c");
	private Map<String, ClassA> map2 = newMap2();
	private List<ClassA> list2 = Lists.newArrayList(new ClassA());
	private Long[] longs1 = new Long[] {10L, 11L, 12L};
	private ClassA[] classAs1 = new ClassA[] {new ClassA()};
	
	protected Map<String, String> newMap1() {
		Map<String, String> map = new HashMap<>();
		map.put("hello", "world");
		return map;
	}
	
	protected Map<String, ClassA> newMap2() {
		Map<String, ClassA> map = new HashMap<>();
		map.put("value", new ClassA());
		return map;
	}
	
}
