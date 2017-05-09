package com.tvd12.ezyfoxserver.testing.performance;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class NewArrayTest extends BaseTest {

	@Test
	public void test1() {
//		EzyFactory.create(EzyArrayBuilder.class);
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			EzyEntityFactory.create(EzyArrayBuilder.class);
		})
		.getTime();
		System.out.println("test1 elapsed time = " + time);
	}
	
	@Test
	public void test2() {
		EzyEntityFactory.create(EzyArrayBuilder.class);
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			EzyArrayBuilder builder = EzyEntityFactory.create(EzyArrayBuilder.class);
			builder.append((Object)null);
			builder.append("");
			builder.append(1);
			builder.append(new HashMap<>());
		})
		.getTime();
		System.out.println("test2 elapsed time = " + time);
	}
	
	
}
