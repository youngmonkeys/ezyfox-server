package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyObjects;
import com.tvd12.test.base.BaseTest;

import lombok.EqualsAndHashCode;

public class EzyObjectsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyObjects.class;
	}
	
	@Test
	public void test() {
		assert EzyObjects.equals(null, null);
		assert !EzyObjects.equals(null, this);
		assert !EzyObjects.equals(this, null);
		assert EzyObjects.equals(new ClassA(), new ClassA());
	}

	@EqualsAndHashCode
	public static class ClassA {
	}
	
}
