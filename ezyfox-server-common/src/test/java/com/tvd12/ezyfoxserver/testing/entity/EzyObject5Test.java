package com.tvd12.ezyfoxserver.testing.entity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.testing.CommonBaseTest;

public class EzyObject5Test extends CommonBaseTest {

	@Test
	public void test1() {
		EzyObject object = newObjectBuilder()
				.append("1", ABC.A)
				.build();
		assertEquals(object.get("1", ABC.class), ABC.A);
	}
	
	public static enum ABC {
		A,B,C
	}
}
