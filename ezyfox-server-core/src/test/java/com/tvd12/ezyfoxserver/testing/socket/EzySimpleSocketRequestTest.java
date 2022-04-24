package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequest;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class EzySimpleSocketRequestTest extends BaseTest {

    @Test
    public void test() {
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append(EzyCommand.APP_ACCESS.getId())
            .build();
        EzySimpleSocketRequest request = new EzySimpleSocketRequest(session, data);

        assert request.isSystemRequest();
    }

}
