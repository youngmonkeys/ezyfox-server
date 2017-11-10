package com.tvd12.ezyfoxserver.testing.codec;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.codec.EzyDecodeState;
import com.tvd12.ezyfoxserver.codec.EzyIDecodeState;
import com.tvd12.test.base.BaseTest;

public class EzyDecodeStateTest extends BaseTest {

	@Test
	public void test() {
		EzyIDecodeState state = EzyDecodeState.PREPARE_MESSAGE;
		assert state.getId() == 0;
		EzyDecodeState.values();
		EzyDecodeState.valueOf("PREPARE_MESSAGE");
	}
	
}
