package com.tvd12.ezyfoxserver.database.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.database.service.EzySaveManyService;

public class EzySaveManyServiceTest {

	@Test
	public void test() {
		new EzySaveManyService<Object>() {
			public void save(java.lang.Iterable<Object> entities) {};
		}
		.save(new Object(), new Object());
	}
	
}
