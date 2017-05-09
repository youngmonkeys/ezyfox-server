package com.tvd12.ezyfoxserver.codec.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.codec.MsgPackType;
import com.tvd12.test.base.BaseTest;

public class MsgPackTypeTest extends BaseTest {

	@Test
	public void test() {
		assert MsgPackType.POSITIVE_FIXINT.getId() == 0;
		assert MsgPackType.POSITIVE_FIXINT.getName().equals("POSITIVE_FIXINT");
		MsgPackType.values();
		MsgPackType.valueOf("POSITIVE_FIXINT");
	}
	
}
