package com.tvd12.ezyfoxserver.testing.util;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyEntityArrays;
import com.tvd12.test.base.BaseTest;

public class EzyEntityArraysTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyEntityArrays.class;
	}
	
	@Test
	public void test() {
		EzyEntityArrays.newArray(new ArrayList<>());
	}
	
}
