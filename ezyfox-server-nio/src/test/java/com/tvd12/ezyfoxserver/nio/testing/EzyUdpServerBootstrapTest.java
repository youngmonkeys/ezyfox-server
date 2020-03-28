package com.tvd12.ezyfoxserver.nio.testing;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.EzyUdpServerBootstrap;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;

public class EzyUdpServerBootstrapTest {

	@Test
	public void test() throws Exception {
		EzySimpleServerContext context = new EzySimpleServerContext();
		EzySimpleServer server = new EzySimpleServer();
		EzySimpleSettings settings = new EzySimpleSettings();
		server.setSettings(settings);
		context.setServer(server);
		EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
		EzyUdpServerBootstrap bootstrap = EzyUdpServerBootstrap.builder()
				.serverContext(context)
				.handlerGroupManager(handlerGroupManager)
				.build();
		bootstrap.start();
		Thread.sleep(250);
		bootstrap.destroy();
				
	}
	
}
