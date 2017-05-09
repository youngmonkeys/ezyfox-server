package com.tvd12.ezyfoxserver.testing.io;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.io.EzyArrays;
import com.tvd12.test.base.BaseTest;

public class EzyArraysTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyArrays.class;
	}
	
	@Test
	public void test() {
		Collection<String> coll = Lists.newArrayList("1", "2", "3");
		Long[] array = EzyArrays.newArray(coll, 
				(s) -> new Long[s], 
				s -> Long.valueOf(s));
		Assert.assertEquals(array, new Long[] {1L, 2L, 3L});
	}
	
	@Test
	public void test1() {
		assert EzyArrays.min(new Long[] {3L,1L,2L}) == 1L;
		assert EzyArrays.max(new Long[] {3L,1L,5L,6L,2L}) == 6L;
	}
}
