package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyPriorityBlockingSocketRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequest;
import static org.mockito.Mockito.*;

public class EzySocketRequestComparatorTest {

    @Test
    public void test() throws Exception {
        EzyPriorityBlockingSocketRequestQueue queue = new EzyPriorityBlockingSocketRequestQueue();
        queue.add(newRequest(EzyCommand.PING));
        queue.add(newRequest(EzyCommand.HANDSHAKE));
        queue.add(newRequest(EzyCommand.APP_ACCESS));
        queue.add(newRequest(EzyCommand.APP_REQUEST));
        queue.add(newRequest(EzyCommand.LOGIN));
        assert queue.take().getCommand() == EzyCommand.HANDSHAKE;
        assert queue.take().getCommand() == EzyCommand.LOGIN;
        assert queue.take().getCommand() == EzyCommand.APP_ACCESS;
        queue.add(newRequest(EzyCommand.LOGIN));
        queue.add(newRequest(EzyCommand.LOGIN));
        queue.add(newRequest(EzyCommand.LOGIN));
        Thread.sleep(10);
        queue.add(newRequest(EzyCommand.LOGIN));
        Thread.sleep(10);
        queue.add(newRequest(EzyCommand.LOGIN));
        assert queue.take().getCommand() == EzyCommand.LOGIN;
    }
    
    private EzySimpleSocketRequest newRequest(EzyCommand cmd) {
        EzySession session = mock(EzySession.class);
        EzyArray data = EzyEntityFactory.newArrayBuilder()
                .append(cmd.getId())
                .build();
        EzySimpleSocketRequest request = new EzySimpleSocketRequest(session, data);
        return request;
    }
    
}
