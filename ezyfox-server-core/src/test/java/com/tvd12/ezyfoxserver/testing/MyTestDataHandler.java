package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;

public class MyTestDataHandler extends EzySimpleDataHandler<MyTestSession> {
    
    public MyTestDataHandler(EzyServerContext ctx, MyTestSession session) {
        super(ctx, session);
    }

    public void provideSession() {
        MyTestSession session1 = session != null ? session : new MyTestSession();
        session1.setToken("abc");
    }
}
