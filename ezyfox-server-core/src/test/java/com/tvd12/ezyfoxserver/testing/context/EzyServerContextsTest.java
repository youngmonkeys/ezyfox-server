package com.tvd12.ezyfoxserver.testing.context;

import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyServerContextsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleApplication app = new EzySimpleApplication();
        app.setUserManager(EzyAppUserManagerImpl.builder()
            .maxUsers(1)
            .appName("test")
            .build());
        EzyAppContext appContext = mock(EzyAppContext.class);
        when(appContext.getApp()).thenReturn(app);
        EzySimpleUser user = new EzySimpleUser();
        assert !EzyServerContexts.containsUser(appContext, user);
        assert !EzyServerContexts.containsUser(appContext, "test");
        assert EzyServerContexts.getUserManager(appContext) != null;
    }

    @Override
    public Class<?> getTestClass() {
        return EzyServerContexts.class;
    }

}
