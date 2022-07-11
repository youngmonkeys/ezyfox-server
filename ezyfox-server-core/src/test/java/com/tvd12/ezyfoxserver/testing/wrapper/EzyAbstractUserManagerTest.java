package com.tvd12.ezyfoxserver.testing.wrapper;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractUserManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyAbstractUserManagerTest extends BaseTest {

    @Test
    public void availableTest() {
        // given
        InternalUserManager sut = new InternalUserManagerBuilder()
            .build();

        // when
        boolean actual = sut.available();

        // then
        Asserts.assertTrue(actual);
    }

    @Test
    public void unAvailableTest() {
        // given
        InternalUserManager sut = new InternalUserManagerBuilder()
            .maxUsers(1)
            .build();

        EzyUser user = mock(EzyUser.class);
        String username = RandomUtil.randomShortAlphabetString();
        when(user.getName()).thenReturn(username);
        sut.addUser(user);

        // when
        boolean actual = sut.available();

        // then
        Asserts.assertFalse(actual);

        verify(user, times(1)).getId();
        verify(user, times(1)).getName();
    }

    @Test
    public void getMessagePrefixTest() {
        // given
        InternalUserManager sut = new InternalUserManagerBuilder()
            .build();

        // when
        String actual = MethodUtil.invokeMethod(
            "getMessagePrefix",
            sut
        );

        // then
        Asserts.assertEquals(actual, "user manager:");
    }

    public static class InternalUserManager extends EzyAbstractUserManager {

        protected InternalUserManager(InternalUserManagerBuilder builder) {
            super(builder);
        }
    }

    public static class InternalUserManagerBuilder
        extends EzyAbstractUserManager.Builder<InternalUserManagerBuilder> {


        @Override
        public InternalUserManager build() {
            return new InternalUserManager(this);
        }
    }
}
