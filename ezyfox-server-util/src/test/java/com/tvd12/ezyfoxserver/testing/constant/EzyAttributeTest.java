package com.tvd12.ezyfoxserver.testing.constant;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyAttribute;
import com.tvd12.test.base.BaseTest;

public class EzyAttributeTest extends BaseTest {

	@Test
	public void test() {
		EzyAttribute<String> A = EzyAttribute.valueOf(1, "a");
		assert A.getName().equals("a");
		EzyAttribute<String> B = EzyAttribute.valueOf(2);
		assert B.getName().equals("attribute#2");
		EzyAttribute<String> C = EzyAttribute.valueOf("c");
		assert C.getId() > 2;
		EzyAttribute<String> D = EzyAttribute.one();
		System.err.println("D.id = " + D.getId() + ", D.name = " + D.getName());
	}
	
}
