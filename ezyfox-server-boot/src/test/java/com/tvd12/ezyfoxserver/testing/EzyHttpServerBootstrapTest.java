package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyEmptyHttpBootstrap;
import com.tvd12.ezyfoxserver.EzyHttpServerBootstrap;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;

import static org.mockito.Mockito.*;

public class EzyHttpServerBootstrapTest extends BaseTest {

	@Test
	public void test() {
		EzyServerContext serverContext = mock(EzyServerContext.class);
		EzySimpleSettings settings = new EzySimpleSettings();
		EzySimpleServer server = new EzySimpleServer();
		server.setSettings(settings);
		when(serverContext.getServer()).thenReturn(server);
		EzyEmptyHttpBootstrap httpBootstrap = new EzyEmptyHttpBootstrap();
		httpBootstrap.setServerContext(serverContext);
		ExEzyHttpServerBootstrap bootstrap = new ExEzyHttpServerBootstrap();
		bootstrap.setContext(serverContext);
		MethodInvoker.create()
			.object(bootstrap)
			.method("startHttpBootstrap")
			.invoke();
		
		settings.getHttp().setActive(true);
		MethodInvoker.create()
			.object(bootstrap)
			.method("startHttpBootstrap")
			.invoke();
		bootstrap.destroy();
		bootstrap.destroy();
	}
	
	public static class ExEzyHttpServerBootstrap extends EzyHttpServerBootstrap {

		@Override
		protected void startOtherBootstraps(Runnable callback) throws Exception {
		}
		
	}
	
}
