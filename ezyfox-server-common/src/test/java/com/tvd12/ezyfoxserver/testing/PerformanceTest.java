package com.tvd12.ezyfoxserver.testing;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.test.base.BaseTest;

public class PerformanceTest extends BaseTest {

	@SuppressWarnings("unused")
	public static void main(String args[]) {
		System.out.println("\n========= begin =========\n");
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < 10000000 ; i++) {
			Map<String, String> strs = new HashMap<>();
			strs.put("a", "1");
			strs.put("b", "2");
			strs.put("c", "3");
			strs.put("d", "4");
			strs.put("e", "5");
			
			String a = strs.get("a");
			String b = strs.get("b");
			String c = strs.get("c");
			String d = strs.get("d");
			String e = strs.get("e");
		}
		long end = System.currentTimeMillis();
		System.out.println("time = " + (end - start));
		System.out.println("\n========= end =========\n");
	}
	
}
