package com.tvd12.ezyfoxserver.testing.session;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.factory.EzyAbstractSessionFactory;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

import java.util.ArrayList;
import java.util.List;

public class EzySimpleSessionManagerTest {

    public static void main(String[] args) throws Exception {
        MySessionManager manager = MySessionManager.builder()
            .build();
//        manager.start();
        Thread.sleep(30 * 1000);
        List<MySession> sessions = new ArrayList<>();
        System.out.println("add sessions");
        for (int i = 0; i < 500; ++i) {
            sessions.add(manager.provideSession());
        }
        Thread.sleep(30 * 1000);
        System.out.println("return sessions");
        for (MySession session : sessions) {
            manager.removeSession(session);
        }
        System.out.println("finish");
        Thread.sleep(300 * 1000);
    }

    public static class MySession extends EzyAbstractSession {
        private static final long serialVersionUID = 6212756561210534733L;

    }

    public static class MySessionFactory extends EzyAbstractSessionFactory<MySession> {

        @Override
        protected MySession newSession() {
            return new MySession();
        }

    }

    public static class MySessionManager extends EzySimpleSessionManager<MySession> {

        protected MySessionManager(Builder builder) {
            super(builder);
        }

        public static Builder builder() {
            return new Builder();
        }

        public MySession provideSession() {
            return provideSession(EzyConnectionType.SOCKET);
        }

        public static class Builder extends EzySimpleSessionManager.Builder<MySession> {

            @Override
            public MySessionManager build() {
                return new MySessionManager(this);
            }

            @Override
            protected EzyObjectFactory<MySession> newObjectFactory() {
                return new MySessionFactory();
            }

        }

    }
}
