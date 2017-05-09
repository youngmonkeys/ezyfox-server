package com.tvd12.ezyfoxserver.bean.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.tvd12.ezyfoxserver.bean.annotation.EzyConfigurationBefore;

@SuppressWarnings("rawtypes")
public class EzyConfigurationBeforeClassSorter {

	private EzyConfigurationBeforeClassSorter() {
	}
	
	public static List<Class> sort(Collection<Class> classes) {
		List<Class> list = new ArrayList<>(classes);
		list.sort(newComparator());
		return list;
	}
	
	private static Comparator<Class> newComparator() {
		return (c1, c2) -> getPriority(c1) - getPriority(c2);
	}
	
	private static int getPriority(Class<?> clazz) {
		return getPriority(clazz.getAnnotation(EzyConfigurationBefore.class));
	}
	
	private static int getPriority(EzyConfigurationBefore annotation) {
		return annotation.priority();
	}
}
