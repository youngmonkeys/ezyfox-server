package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;

public class MyTestDataHandler extends EzySimpleDataHandler<MyTestSession> {
    
    public MyTestDataHandler() {
    }
    
    public MyTestDataHandler(MyTestSession session) {
        this.session = session;
    }
    
    public MyTestDataHandler(EzySession session) {
        this((MyTestSession)session);
    }

    public void provideSession() {
        MyTestSession session1 = session != null ? session : new MyTestSession();
        session1.setReconnectToken("abc");
        provideSession(() -> session1);
    }
    
    @Override
    public EzyAppContext getAppContext(int appId) {
        return super.getAppContext(appId);
    }
}
