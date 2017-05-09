package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.io.EzyIterables;
import com.tvd12.test.base.BaseTest;

public class EzyIterablesTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyIterables.class;
	}
	
	@Test
	public void test() {
		assert EzyIterables.isEmpty(null);
		assert EzyIterables.isEmpty(Lists.newArrayList());
		assert !EzyIterables.isEmpty(Lists.newArrayList(1));
	}
	
}
