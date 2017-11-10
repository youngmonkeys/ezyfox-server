package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.factory.EzyAbstractSessionFactory;

public class MyTestSessionFactory 
        extends EzyAbstractSessionFactory<MyTestSession> {

    @Override
    protected MyTestSession newSession() {
        return new MyTestSession();
    }
    
}
