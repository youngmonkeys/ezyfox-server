package com.tvd12.ezyfoxserver.nio.testing.builder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerCreator;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerCreator.HealthCheckServlet;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandler;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.test.reflect.MethodInvoker;
import static org.mockito.Mockito.*;

public class EzyWebSocketServerCreatorTest {

	@Test
	public void createManagementTest() {
		// given
		EzyWebSocketServerCreator creator = new EzyWebSocketServerCreator();
		EzySimpleWebSocketSetting webSocketSetting = new EzySimpleWebSocketSetting();
		webSocketSetting.setManagementEnable(true);
		
		creator.setting(webSocketSetting);
		
		// when
		creator.create();
		
		// then
		EzyWsHandler wsHandler = MethodInvoker.create()
				.object(creator)
				.method("newWsHandler")
				.invoke(EzyWsHandler.class);
		
		WebSocketCreator webSocketCreator = MethodInvoker.create()
				.object(creator)
				.method("newWebSocketCreator")
				.param(EzyWsHandler.class, wsHandler)
				.invoke(WebSocketCreator.class);
		webSocketCreator.createWebSocket(null, null);
	}
	
	@Test
	public void healthCheckServletTest() {
		// given
		HealthCheckServlet sut = new HealthCheckServlet();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		// when
		MethodInvoker.create()
			.object(sut)
			.method("doGet")
			.param(HttpServletRequest.class, request)
			.param(HttpServletResponse.class, response)
			.call();
		
		// then
		verify(response, times(1)).setStatus(200);
	}
}
