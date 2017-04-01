package com.tvd12.ezyfoxserver.testing;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.test.base.BaseTest;

public class Performance2Test extends BaseTest {

	@SuppressWarnings({ "unused", "rawtypes" })
	public static void main(String args[]) {
		System.out.println("\n========= begin =========\n");
		long start = System.currentTimeMillis();
		Map<String, String> strs = new HashMap<>();
		for(int i = 0 ; i < 10000000 ; i++) {
			HashMap hm = (HashMap)strs;
		}
		long end = System.currentTimeMillis();
		System.out.println("time = " + (end - start));
		System.out.println("\n========= end =========\n");
	}
	
}
