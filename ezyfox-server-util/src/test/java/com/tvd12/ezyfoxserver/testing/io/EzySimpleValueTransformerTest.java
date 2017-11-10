package com.tvd12.ezyfoxserver.testing.io;

import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.io.EzySimpleValueConverter;
import com.tvd12.ezyfoxserver.io.EzyValueConverter;
import com.tvd12.test.base.BaseTest;

public class EzySimpleValueTransformerTest extends BaseTest {

	@Test
	public void test() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		assert transformer.convert(null, null) == null;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	public void test1() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		List<Integer> v = transformer.convert(Lists.newArrayList(1, 2, 3), List.class);
	}
	
}
