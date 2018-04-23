package com.tvd12.ezyfoxserver.testing.delegate;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.delegate.EzyAbstractSessionDelegate;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.test.base.BaseTest;

public class EzySessionAdapterTest extends BaseTest {

    @Test
    public void test() {
        EzySessionDelegate delegate = new EzyAbstractSessionDelegate() {
        };
        delegate.onSessionRemoved(EzyDisconnectReason.ANOTHER_SESSION_LOGIN);
        delegate.onSessionLoggedIn(new EzySimpleUser());
    }
    
}
