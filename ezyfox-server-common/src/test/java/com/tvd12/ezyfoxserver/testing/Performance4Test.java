package com.tvd12.ezyfoxserver.testing;

import lombok.Builder;

public class Performance4Test {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < 10000000 ; i++) {
			ClassA.builder()
				.a("a")
				.b("b")
				.c("c")
				.d("d")
				.e("e")
				.build();
		}
		long end = System.currentTimeMillis();
		System.out.println("time elapsed: " + (end - start));
	}
	
	@SuppressWarnings("unused")
	@Builder
	public static class ClassA {
		private String a;
		private String b;
		private String c;
		private String d;
		private String e;
	}
	
}
