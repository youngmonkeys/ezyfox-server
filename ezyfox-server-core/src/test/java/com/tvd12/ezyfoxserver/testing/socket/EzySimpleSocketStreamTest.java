package com.tvd12.ezyfoxserver.testing.socket;

import static org.mockito.Mockito.spy;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketStream;
import com.tvd12.test.base.BaseTest;

public class EzySimpleSocketStreamTest extends BaseTest {

    @Test
    public void test() {
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleSocketStream stream = new EzySimpleSocketStream(session, new byte[0]);
        System.out.println(stream.getTimestamp());
    }
    
}
