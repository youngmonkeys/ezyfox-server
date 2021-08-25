package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractZoneEventController;

public class EzyAbstractZoneEventControllerTest {

	@Test
	public void newTest() {
		new EzyAbstractZoneEventController<Object>() {

			@Override
			public void handle(EzyZoneContext ctx, Object event) {
			}
		};
	}
}
