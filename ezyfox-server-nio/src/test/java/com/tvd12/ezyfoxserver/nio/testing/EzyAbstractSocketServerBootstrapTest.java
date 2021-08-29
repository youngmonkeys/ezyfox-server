package com.tvd12.ezyfoxserver.nio.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.nio.EzyAbstractSocketServerBootstrap;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.test.reflect.FieldUtil;

public class EzyAbstractSocketServerBootstrapTest {

	@Test
	public void destroyTest() {
		// given
		EzySocketEventLoopHandler writingLoopHandler = mock(EzySocketEventLoopHandler.class);
		InternalBoostrap sut = new InternalBoostrap.Builder()
				.build();
		FieldUtil.setFieldValue(sut, "writingLoopHandler", writingLoopHandler);
		
		// when
		sut.destroy();
		
		// then
		verify(writingLoopHandler, times(1)).destroy();
	}
	
	private static class InternalBoostrap extends EzyAbstractSocketServerBootstrap {

		public InternalBoostrap(Builder builder) {
			super(builder);
		}
	
		public static class Builder extends EzyAbstractSocketServerBootstrap.Builder<Builder, InternalBoostrap> {
			@Override
			public InternalBoostrap build() {
				return new InternalBoostrap(this);
			}
		}

		@Override
		public void start() throws Exception {
		}
	}
}
