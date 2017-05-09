package com.tvd12.ezyfoxserver.testing.util;

import org.reflections.Reflections;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.test.base.BaseTest;

public class ReflectionsTest extends BaseTest {

	@Test
	public void test() {
		new Reflections("non-exists").getTypesAnnotatedWith(EzyAutoImpl.class);
	}
	
}
