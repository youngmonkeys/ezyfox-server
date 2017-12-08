package com.tvd12.ezyfoxserver.testing.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyNameStyles;
import com.tvd12.test.base.BaseTest;

public class EzyNameStylesTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyNameStyles.class;
	}
	
	@Test
	public void test() {
		assertEquals(EzyNameStyles.toLowerHyphen("EzyFox"), "ezy-fox");
		assertEquals(EzyNameStyles.toLowerHyphen("ezyFox"), "ezy-fox");
		assertEquals(EzyNameStyles.toLowerHyphen("Ezyfox"), "ezyfox");
		assertEquals(EzyNameStyles.toLowerHyphen("ezyfox"), "ezyfox");
	}
	
}
