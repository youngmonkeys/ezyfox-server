package com.tvd12.ezyfoxserver.testing.handler;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestDataHandler;

public class EzySimpleDataHandlerTest extends BaseCoreTest {
    @Test
    public void test1() throws Exception {
        EzyServerContext context = newServerContext();
        EzyAppContext appContext = context.getAppContext("ezyfox-chat");
        MyTestDataHandler handler = new MyTestDataHandler();
        handler.setContext(context);
        handler.getAppContext(appContext.getApp().getSetting().getId());
        handler.borrowSession();
        handler.sessionActive();
        
        EzyArray message = newArrayBuilder()
                .append(EzyCommand.LOGIN.getId())
                .append(newArrayBuilder()
                        .append("")
                        .append("")
                        .append(newArrayBuilder()))
                .build();
        
        handler.dataReceived(message);
        handler.channelInactive();
        handler.onSessionReturned(EzyDisconnectReason.NOT_LOGGED_IN);
    }
    
    @Test
    public void test2() throws Exception {
        EzySession session = newSession();
        session.setReconnectToken("123");
        EzySimpleUser user = newUser();
        user.setName("dungtv");
        user.addSession(session);
        EzyServerContext context = newServerContext();
        MyTestDataHandler handler = new MyTestDataHandler(session);
        handler.setContext(context);
        handler.borrowSession();
        handler.sessionActive();
        handler.onSessionLoggedIn(user);
        
        EzyArray message = newArrayBuilder()
                .append(EzyCommand.APP_ACCESS.getId())
                .append(newArrayBuilder()
                        .append("ezyfox-chat"))
                .build();
        
        handler.dataReceived(message);
        handler.onSessionReturned(EzyDisconnectReason.NOT_LOGGED_IN);
    }
    
    @Test
    public void test3() throws Exception {
        EzyServerContext context = newServerContext();
        MyTestDataHandler handler = new MyTestDataHandler();
        handler.setContext(context);
        handler.borrowSession();
        handler.sessionActive();
        
        EzyArray message = newArrayBuilder()
                .append(EzyCommand.LOGIN.getId())
                .append(newArrayBuilder()
                        .append(""))
                .build();
        
        handler.dataReceived(message);
        handler.channelInactive();
        handler.onSessionReturned(EzyDisconnectReason.NOT_LOGGED_IN);
    }
}
