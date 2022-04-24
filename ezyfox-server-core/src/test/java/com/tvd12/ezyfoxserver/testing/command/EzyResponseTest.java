package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.command.impl.EzyAppResponseImpl;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

public class EzyResponseTest {

    @Test
    public void test() {
        // given
        String username1 = RandomUtil.randomShortAlphabetString();
        String username2 = RandomUtil.randomShortAlphabetString();

        EzySession session1 = mock(EzySession.class);
        EzySession session2 = mock(EzySession.class);

        EzyUser user1 = mock(EzyUser.class);
        when(user1.getSessions()).thenReturn(Collections.singletonList(session1));

        EzyUser user2 = mock(EzyUser.class);
        when(user2.getSessions()).thenReturn(Collections.singletonList(session2));


        EzyAppUserManager userManager = mock(EzyAppUserManager.class);
        when(userManager.getUser(username1)).thenReturn(user1);
        when(userManager.getUser(username2)).thenReturn(user2);

        EzyApplication app = mock(EzyApplication.class);
        when(app.getUserManager()).thenReturn(userManager);

        EzyAppContext appContext = mock(EzyAppContext.class);
        when(appContext.getApp()).thenReturn(app);

        // when
        EzyResponse sut = new EzyAppResponseImpl(appContext)
            .usernames(username1, username2)
            .usernames(Arrays.asList(username1, username2));

        // then
        Set<EzySession> recipients = FieldUtil.getFieldValue(sut, "recipients");
        Asserts.assertEquals(recipients, Sets.newHashSet(session1, session2));

        verify(user1, times(2)).getSessions();
        verify(user2, times(2)).getSessions();
        verify(appContext, times(1)).getApp();
        verify(app, times(1)).getUserManager();
        verify(userManager, times(2)).getUser(username1);
        verify(userManager, times(2)).getUser(username2);
    }
}
