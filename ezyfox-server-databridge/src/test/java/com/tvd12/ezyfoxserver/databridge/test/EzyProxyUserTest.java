package com.tvd12.ezyfoxserver.databridge.test;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyUser;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import static org.mockito.Mockito.*;

public class EzyProxyUserTest {

	@Test
	public void test() {
		EzySimpleUser user = new EzySimpleUser();
		user.setName("test");
		EzyAbstractSession session = spy(EzyAbstractSession.class);
		user.addSession(session);
		EzyProxyUser proxy = new EzyProxyUser(user);
		EzyProxyUser.newCollection(Lists.newArrayList(user));
		assert proxy.getId() == user.getId();
		assert proxy.getName().equals("test");
		assert proxy.getSessions().size() == 1;
	}
	
}
