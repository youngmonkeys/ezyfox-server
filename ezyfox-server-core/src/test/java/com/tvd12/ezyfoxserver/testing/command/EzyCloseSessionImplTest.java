package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.command.impl.EzyCloseSessionImpl;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzyCloseSessionImplTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyCloseSessionImpl cmd = new EzyCloseSessionImpl(serverContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        session.setChannel(channel);
        cmd.close(session, EzyDisconnectReason.ADMIN_BAN);
    }
    
    @Test
    public void noSendToClient() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyCloseSessionImpl cmd = new EzyCloseSessionImpl(serverContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        session.setChannel(channel);
        cmd.close(session, EzyDisconnectReason.UNKNOWN);
    }
    
}
