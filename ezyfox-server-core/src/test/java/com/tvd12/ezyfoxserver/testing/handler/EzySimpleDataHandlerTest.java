package com.tvd12.ezyfoxserver.testing.handler;

import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleDataHandlerTest extends BaseCoreTest {
//    @Test
//    public void test1() throws Exception {
//        EzyServerContext context = newServerContext();
//        EzyZoneContext zoneContext = context.getZoneContext("example");
//        EzyAppContext appContext = zoneContext.getAppContext("ezyfox-chat");
//        MyTestDataHandler handler = new MyTestDataHandler();
//        handler.setContext(context);
//        handler.getAppContext(appContext.getApp().getSetting().getId());
//        handler.provideSession();
//        handler.sessionActive();
//        
//        EzyArray message = newArrayBuilder()
//                .append(EzyCommand.LOGIN.getId())
//                .append(newArrayBuilder()
//                        .append("")
//                        .append("")
//                        .append(newArrayBuilder()))
//                .build();
//        
//        handler.dataReceived(EzyCommand.LOGIN, message);
//        handler.channelInactive();
//        handler.onSessionRemoved(EzyDisconnectReason.NOT_LOGGED_IN);
//    }
//    
//    @Test
//    public void test2() throws Exception {
//        EzySession session = newSession();
//        session.setReconnectToken("123");
//        EzySimpleUser user = newUser();
//        user.setName("dungtv");
//        user.addSession(session);
//        EzyServerContext context = newServerContext();
//        user.setZoneId(context.getServer().getSettings().getZoneIds().iterator().next());
//        MyTestDataHandler handler = new MyTestDataHandler(context, (MyTestSession) session);
//        handler.provideSession();
//        handler.sessionActive();
//        handler.onSessionLoggedIn(user);
//        
//        EzyArray message = newArrayBuilder()
//                .append(EzyCommand.APP_ACCESS.getId())
//                .append(newArrayBuilder()
//                        .append("ezyfox-chat"))
//                .build();
//        
//        handler.dataReceived(EzyCommand.APP_ACCESS, message);
//        handler.onSessionRemoved(EzyDisconnectReason.NOT_LOGGED_IN);
//    }
//    
//    @Test
//    public void test3() throws Exception {
//        EzyServerContext context = newServerContext();
//        MyTestDataHandler handler = new MyTestDataHandler();
//        handler.setContext(context);
//        handler.provideSession();
//        handler.sessionActive();
//        
//        EzyArray message = newArrayBuilder()
//                .append(EzyCommand.LOGIN.getId())
//                .append(newArrayBuilder()
//                        .append(""))
//                .build();
//        
//        handler.dataReceived(EzyCommand.LOGIN, message);
//        handler.channelInactive();
//        handler.onSessionRemoved(EzyDisconnectReason.NOT_LOGGED_IN);
//    }
}
