package com.tvd12.ezyfoxserver.testing.performance;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.test.performance.Performance;

public class EzyMapsTest {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Map<Class, Object> map = new HashMap<>();
		map.put(A.class, new E());
		long time = Performance.create()
			.loop(1000000)
			.test(() -> EzyMaps.getValue(map, A.class))
			.getTime();
		System.out.println(time);
	}
	
	public static interface A {
	}
	
	public static interface B extends A {
		
	}
	
	public static interface C extends A {
		
	}
	
	public static class D implements C {
		
	}
	
	public static class E extends D {
		
	}
	
}
