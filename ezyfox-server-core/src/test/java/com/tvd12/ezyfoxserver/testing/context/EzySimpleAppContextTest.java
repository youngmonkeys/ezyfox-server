package com.tvd12.ezyfoxserver.testing.context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleAppContextTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzySimpleZone zone = new EzySimpleZone();
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);
        when(zoneContext.getParent()).thenReturn(serverContext);
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        app.setSetting(setting);
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        appContext.setParent(zoneContext);
        appContext.setApp(app);
        appContext.init();
        assert appContext.get(Void.class) == null;
        assert appContext.equals(appContext);
        EzySimpleAppContext appContext2 = new EzySimpleAppContext();
        assert !appContext.equals(appContext2);
        assert appContext.cmd(EzyAppResponse.class) != null;
        
        assert appContext.cmd(Void.class) == null;;
        
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        user.addSession(session);
        
        EzyData data = EzyEntityFactory.newArrayBuilder()
                .build();
        appContext.send(data, session, false);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        appContext.setExecutorService(executorService);
        assert appContext.getExecutorService() != null;
        
        appContext.handleException(Thread.currentThread(), new Exception());
    }
    
}
