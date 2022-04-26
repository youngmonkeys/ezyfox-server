package com.tvd12.ezyfoxserver.testing.delegate;

import com.tvd12.ezyfoxserver.delegate.EzyAbstractSessionDelegate;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySessionAdapterTest extends BaseTest {

    @Test
    public void test() {
        EzySessionDelegate delegate = new EzyAbstractSessionDelegate() {};
        delegate.onSessionLoggedIn(new EzySimpleUser());
    }
}
