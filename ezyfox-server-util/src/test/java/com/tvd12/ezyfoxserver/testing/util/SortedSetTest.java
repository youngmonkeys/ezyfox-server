package com.tvd12.ezyfoxserver.testing.util;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

public class SortedSetTest {

	public static void main(String[] args) {
		int size = 1000000;
		Set<String> normalSet = new HashSet<>();
//		SortedSet<String> sortedSet = new TreeSet<>();
		for(int i = 0 ; i < size; i++) {
			normalSet.add(i + "");
//			sortedSet.add(i + "");
		}
		
		System.out.println("setup done");
		
		Set<String> find = Sets.newHashSet("100", "123", "345", "1", "10", "999999990000");
		long startTime1 = System.currentTimeMillis();
		for(int i = 0 ; i < 100000000 ; i++) {
			normalSet.containsAll(find);
		}
		long endTime1 = System.currentTimeMillis();
		
		long startTime2 = System.currentTimeMillis();
//		for(int i = 0 ; i < 1000000 ; i++) {
//			sortedSet.containsAll(find);
//		}
		long endTime2 = System.currentTimeMillis();
		
		long offset1 = endTime1 - startTime1;
		long offset2 = endTime2 - startTime2;
		
		System.out.println("offset1 = " + offset1 + ", ofsset2 = " + offset2);
		
	}
	
}
