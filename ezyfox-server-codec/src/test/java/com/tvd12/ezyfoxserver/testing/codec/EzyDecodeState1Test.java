package com.tvd12.ezyfoxserver.testing.codec;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.codec.EzyDecodeState;
import com.tvd12.ezyfoxserver.codec.EzyIDecodeState;
import com.tvd12.test.base.BaseTest;

public class EzyDecodeState1Test extends BaseTest {

	@Test
	public void test() {
		EzyIDecodeState state = EzyDecodeState.PREPARE_MESSAGE;
		assert state.getId() == 0;
		state = EzyDecodeState.PREPARE_MESSAGE;
		state = EzyDecodeState.READ_MESSAGE_HEADER;
		state = EzyDecodeState.READ_MESSAGE_SIZE;
		state = EzyDecodeState.READ_MESSAGE_CONTENT;
	}
	
}
