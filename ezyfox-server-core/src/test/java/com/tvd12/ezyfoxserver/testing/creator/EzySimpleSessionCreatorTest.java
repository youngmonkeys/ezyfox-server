package com.tvd12.ezyfoxserver.testing.creator;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.creator.EzySessionCreator;
import com.tvd12.ezyfoxserver.creator.EzySimpleSessionCreator;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzySimpleSessionCreatorTest extends BaseTest {
    
    @SuppressWarnings("rawtypes")
    @Test
    public void test() {
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        EzySimpleSessionManagementSetting sessionSetting = new EzySimpleSessionManagementSetting();
        EzySessionCreator creator = EzySimpleSessionCreator.builder()
                .sessionManager(sessionManager)
                .sessionSetting(sessionSetting)
                .build();
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        when(sessionManager.provideSession(any())).thenReturn(session);
        assert creator.create(mock(EzyChannel.class)) == session;
                
    }

}
