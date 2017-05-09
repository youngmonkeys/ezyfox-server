package com.tvd12.ezyfoxserver.testing;

import java.util.List;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.factory.EzyAbstractSessionFactory;
import com.tvd12.ezyfoxserver.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class MyTestSessionManager extends EzySimpleSessionManager<MyTestSession> {

    protected MyTestSessionManager(Builder builder) {
        super(builder);
    }
    
    @Override
    public List<MyTestSession> getBorrowedObjects() {
        return super.getBorrowedObjects();
    }
    
    @Override
    public MyTestSession borrowSession(EzyConnectionType type) {
        return super.borrowSession(type);
    }
    
    @Override
    public boolean isStaleObject(MyTestSession session) {
        return super.isStaleObject(session);
    }
    
    @Override
    public void removeStaleObject(MyTestSession session) {
        super.removeStaleObject(session);
    }
    
    @Override
    public List<MyTestSession> getCanBeStaleObjects() {
        return super.getCanBeStaleObjects();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzySimpleSessionManager.Builder<MyTestSession> {

        @Override
        public MyTestSessionManager build() {
            return new MyTestSessionManager(this);
        }

        @Override
        protected EzyObjectFactory<MyTestSession> newObjectFactory() {
            return new EzyAbstractSessionFactory<MyTestSession>() {
                @Override
                protected MyTestSession newSession() {
                    return new MyTestSession();
                }
            };
        }
        
    }

}
