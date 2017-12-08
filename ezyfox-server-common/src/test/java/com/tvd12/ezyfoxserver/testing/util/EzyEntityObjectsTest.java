package com.tvd12.ezyfoxserver.testing.util;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyEntityObjects;
import com.tvd12.test.base.BaseTest;

public class EzyEntityObjectsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyEntityObjects.class;
	}
	
	@Test
	public void test() {
		EzyEntityObjects.newObject(new HashMap<>());
	}
	
}
