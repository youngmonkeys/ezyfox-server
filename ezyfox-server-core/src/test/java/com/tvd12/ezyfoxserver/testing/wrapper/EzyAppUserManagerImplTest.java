package com.tvd12.ezyfoxserver.testing.wrapper;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.delegate.EzyAppUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzyAppUserManagerImplTest extends BaseTest {

    @Test
    public void test() {
        EzyAppUserDelegate userDelegate = mock(EzyAppUserDelegate.class);
        EzyAppUserManager manager = EzyAppUserManagerImpl.builder()
                .appName("test")
                .userDelegate(userDelegate)
                .build();
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        manager.removeUser(user, EzyUserRemoveReason.EXIT_APP);
        manager.addUser(user);
        manager.removeUser(user, EzyUserRemoveReason.EXIT_APP);
    }
    
}
