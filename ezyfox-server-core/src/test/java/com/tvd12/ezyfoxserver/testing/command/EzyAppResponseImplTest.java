package com.tvd12.ezyfoxserver.testing.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyAbstractResponse;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.command.impl.EzyAppResponseImpl;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class EzyAppResponseImplTest extends BaseTest {

    @Test
    public void testPerformance() {
        EzyAppResponse response = newResponse();
        
        EzyUser user1 = new ExUser("user1");
        EzySession session1 = new ExSession();
        EzySession session2 = new ExSession();
        
        user1.addSession(session1);
        user1.addSession(session2);
        
        EzyUser user2 = new ExUser("user2");
        EzySession session3 = new ExSession();
        EzySession session4 = new ExSession();
        
        user2.addSession(session3);
        user2.addSession(session4);
        
//        List<Object> list = new ArrayList<>();
        
        
        long time = Performance.create()
            .loop(1000000)
            .test(() -> {
                response.users(new EzyUser[] {user1, user2}, false);
//                list.add(user1);
//                list.add(user2);
//                list.add(Arrays.asList(new EzyUser[] {user1, user2}));
//                list.addAll(user1.getSessions());
//                list.addAll(user2.getSessions());
//                response.user(user1);
//                response.user(user2);
            })
            .getTime();
        System.out.println("time = " + time);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        EzyAppContext context = mock(EzyAppContext.class);
        EzyApplication application = mock(EzyApplication.class);
        EzyAppUserManager userManager = EzyAppUserManagerImpl.builder().build();
        when(context.getApp()).thenReturn(application);
        when(application.getUserManager()).thenReturn(userManager);
        EzyAppResponse response = new EzyAppResponseImpl(context);
        
        Field recipients = EzyAbstractResponse.class.getDeclaredField("recipients");
        Field exrecipients = EzyAbstractResponse.class.getDeclaredField("exrecipients");
        recipients.setAccessible(true);
        exrecipients.setAccessible(true);
        assert ((Collection)recipients.get(response)).size() == 0;
        assert ((Collection)exrecipients.get(response)).size() == 0;
        
        EzyUser user1 = newUser("user1");
        response.user(user1);
        assert ((Collection)recipients.get(response)).size() == 2;
        assert ((Collection)exrecipients.get(response)).size() == 0;
        
        EzyUser user2 = newUser("user2");
        response.user(user2);
        response.user(user2, true);
        assert ((Collection)recipients.get(response)).size() == 4;
        assert ((Collection)exrecipients.get(response)).size() == 2;
        
        EzyUser user3 = newUser("user3");
        EzyUser user4 = newUser("user4");
        response.users(user3, user4);
        response.users(new EzyUser[] {user3, null}, true);
        assert ((Collection)recipients.get(response)).size() == 8;
        assert ((Collection)exrecipients.get(response)).size() == 4;
        
        EzyUser user5 = newUser("user5");
        EzyUser user6 = newUser("user6");
        response.users(Lists.newArrayList(user5, user6));
        response.users(Lists.newArrayList(user5, null), true);
        assert ((Collection)recipients.get(response)).size() == 12;
        assert ((Collection)exrecipients.get(response)).size() == 6;
        
        EzyUser user7 = newUser("user7");
        EzyUser user8 = newUser("user8");
        userManager.addUser(user7);
        userManager.addUser(user8);
        response.username("user7");
        response.username("user7", true);
        assert ((Collection)recipients.get(response)).size() == 14;
        assert ((Collection)exrecipients.get(response)).size() == 8;
        
        EzyUser user9 = newUser("user9");
        EzyUser user10 = newUser("user10");
        EzyUser user11 = newUser("user11");
        userManager.addUser(user9);
        userManager.addUser(user10);
        userManager.addUser(user11);
        response.usernames("user9", "user10");
        response.usernames(new String[] {"user11"}, true);
        assert ((Collection)recipients.get(response)).size() == 18;
        assert ((Collection)exrecipients.get(response)).size() == 10;
        
        EzyUser user12 = newUser("user12");
        EzyUser user13 = newUser("user13");
        EzyUser user14 = newUser("user14");
        userManager.addUser(user12);
        userManager.addUser(user13);
        userManager.addUser(user14);
        response.usernames(Lists.newArrayList("user12", "user13"));
        response.usernames(Lists.newArrayList("user14"), true);
        assert ((Collection)recipients.get(response)).size() == 22;
        assert ((Collection)exrecipients.get(response)).size() == 12;
        
        EzySession session1 = new ExSession();
        response.session(session1);
        assert ((Collection)recipients.get(response)).size() == 23;
        assert ((Collection)exrecipients.get(response)).size() == 12;
        
        EzySession session2 = new ExSession();
        EzySession session3 = new ExSession();
        response.sessions(session2, session3);
        response.sessions(new EzySession[] {session3}, true);
        assert ((Collection)recipients.get(response)).size() == 25;
        assert ((Collection)exrecipients.get(response)).size() == 13;
        
        EzySession session4 = new ExSession();
        EzySession session5 = new ExSession();
        response.sessions(Lists.newArrayList(session4, session5));
        response.sessions(Lists.newArrayList(session4, session5), true);
        assert ((Collection)recipients.get(response)).size() == 27;
        assert ((Collection)exrecipients.get(response)).size() == 15;
    }
    
    private EzyUser newUser(String name) {
        EzyUser user2 = new ExUser(name);
        EzySession session3 = new ExSession();
        EzySession session4 = new ExSession();
        user2.addSession(session3);
        user2.addSession(session4);
        return user2;
    }
    
    private EzyAppResponse newResponse() {
        EzyAppContext context = mock(EzyAppContext.class);
        EzyApplication application = mock(EzyApplication.class);
        EzyAppUserManager userManager = EzyAppUserManagerImpl.builder().build();
        when(context.getApp()).thenReturn(application);
        when(application.getUserManager()).thenReturn(userManager);
        EzyAppResponse response = new EzyAppResponseImpl(context);
        return response;
    }
    
    public static class ExUser extends EzySimpleUser {
        private static final long serialVersionUID = 5665904607192806625L;
        
        public ExUser(String name) {
            setName(name);
        }
        
    }
    
    public static class ExSession extends EzyAbstractSession {
        private static final long serialVersionUID = 3390787330201950376L;
        
        public static final AtomicInteger ID_GENTOR = new AtomicInteger(0);
        
        public ExSession() {
            int id = ID_GENTOR.incrementAndGet();
            setId(id);
            setReconnectToken(String.valueOf(id));
        }
        
        @Override
        public SocketAddress getClientAddress() {
            return null;
        }
    
        @Override
        public SocketAddress getServerAddress() {
            return null;
        }
    
        @Override
        public void close() {
        }
    
        @Override
        public void disconnect(EzyConstant disconnectReason) {
        }
        
    }
    
}
