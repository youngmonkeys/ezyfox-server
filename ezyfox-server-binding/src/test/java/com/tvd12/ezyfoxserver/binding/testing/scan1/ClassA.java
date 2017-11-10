package com.tvd12.ezyfoxserver.binding.testing.scan1;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@lombok.ToString
public class ClassA<T> {
	
	private T t = newT();
	
	private List<ClassB> classB = Lists.newArrayList(new ClassB(), new ClassB());
	
	@SuppressWarnings("unchecked")
	protected T newT() {
		return (T)new Integer(10);
	}
	
	@Getter
	@Setter
	@ToString
	public static class ClassB {
		private String a = "a";
		private String b = "b";
		private String c = "c";
	}
	
}
