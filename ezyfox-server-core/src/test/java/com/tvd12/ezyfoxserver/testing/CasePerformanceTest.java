package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.entity.*;
import com.tvd12.test.base.BaseTest;
import lombok.Data;
import org.testng.annotations.Test;

public class CasePerformanceTest extends BaseTest {

    @Test
    public void test() {
        A a = new A();
        EzyUser user = new EzySimpleUser();
        EzySession session = new EzyAbstractSession() {};
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            ((EzyUserAware) a).setUser(user);
            ((EzySessionAware) a).setSession(session);
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("total time = " + time);

    }

    @Data
    private static class A implements EzyUserAware, EzySessionAware {
        private EzyUser user;
        private EzySession session;
    }
}
