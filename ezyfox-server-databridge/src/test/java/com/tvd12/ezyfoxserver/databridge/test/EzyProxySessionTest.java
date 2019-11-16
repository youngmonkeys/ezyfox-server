package com.tvd12.ezyfoxserver.databridge.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxySession;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

public class EzyProxySessionTest {

	@Test
	public void test() {
		EzyAbstractSession session = spy(EzyAbstractSession.class);
		session.setClientId("clientId");
		session.setClientType("test");
		session.setToken("token");
		session.setMaxIdleTime(1);
		session.setMaxWaitingTime(2);
		session.setActivated(true);
		session.setLoggedIn(true);
		EzyChannel channel = mock(EzyChannel.class);
		when(channel.getClientAddress()).thenReturn(null);
		session.setChannel(channel);
		session.setReadBytes(2L);
		session.setWrittenBytes(3L);
		session.setCreationTime(System.currentTimeMillis());
		session.setLastActivityTime(System.currentTimeMillis());
		session.setLastReadTime(System.currentTimeMillis());
		session.setLastWriteTime(System.currentTimeMillis());
		EzyProxySession.newCollection(Lists.newArrayList(session));
		EzyProxySession proxy = EzyProxySession.proxySession(session);
		assert proxy.getId() == session.getId();
		assert proxy.getClientId().equals("clientId");
		assert proxy.getClientType().equals("test");
		assert proxy.getToken().equals("token");
		assert proxy.getMaxIdleTime() == 1;
		assert proxy.getMaxWaitingTime() == 2;
		assert proxy.isActivated();
		assert proxy.isLoggedIn();
		assert proxy.getClientAddress() == null;
		assert proxy.getReadBytes() == 2L;
		assert proxy.getWrittenBytes() == 3L;
		System.out.println(proxy.getCreationTime());
		System.out.println(proxy.getLastActivityTime());
		System.out.println(proxy.getLastReadTime());
		System.out.println(proxy.getLastWriteTime());
	}
	
}
