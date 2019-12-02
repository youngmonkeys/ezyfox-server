package com.tvd12.ezyfoxserver.nio.testing.websocket;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.nio.websocket.EzyWsChannel;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsCloseStatus;
import com.tvd12.test.base.BaseTest;

public class EzyWsChannelTest extends BaseTest {

	@Test
	public void test() throws Exception {
		Session session = mock(Session.class);
		when(session.getLocalAddress()).thenReturn(new InetSocketAddress("0.0.0.0", 3005));
		EzyWsChannel channel = new EzyWsChannel(session);
		assert channel.getSession() == session;
		assert channel.isOpened();
		RemoteEndpoint remoteEndpoint = mock(RemoteEndpoint.class);
		when(session.getRemote()).thenReturn(remoteEndpoint);
		System.out.println(channel.getServerAddress());
		assert channel.isConnected();
		channel.write("hello".getBytes(), true);
		channel.write("hello", false);
		doThrow(new WebSocketException("timeout")).when(remoteEndpoint).sendString("hello");
		channel.write("hello", false);
		channel.disconnect();
		channel.close();
		doThrow(new IllegalStateException("maintain")).when(session).disconnect();
		doThrow(new IllegalStateException("maintain")).when(session).close(EzyWsCloseStatus.CLOSE_BY_SERVER);
		channel.disconnect();
		channel.close();
	}
	
}
