package com.tvd12.ezyfoxserver.testing.context;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.command.EzyAppSendResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.*;

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
        Asserts.assertNull(appContext.get(Void.class));
        //noinspection EqualsWithItself
        assert appContext.equals(appContext);
        EzySimpleAppContext appContext2 = new EzySimpleAppContext();
        assert !appContext.equals(appContext2);
        assert appContext.cmd(EzyAppResponse.class) != null;
        Asserts.assertNull(appContext.cmd(Void.class));
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

    @Test
    public void sendMultiTest() {
        // given
        EzyData data = mock(EzyData.class);
        EzySession recipient = mock(EzySession.class);
        List<EzySession> recipients = Collections.singletonList(recipient);
        boolean encrypted = RandomUtil.randomBoolean();

        EzyAppSendResponse sendResponse = mock(EzyAppSendResponse.class);
        doNothing().when(sendResponse).execute(data, recipients, encrypted, EzyTransportType.TCP);

        EzySimpleAppContext sut = new EzySimpleAppContext();
        FieldUtil.setFieldValue(sut, "sendResponse", sendResponse);

        // when
        sut.send(data, recipients, encrypted, EzyTransportType.TCP);

        // then
        verify(sendResponse, times(1)).execute(data, recipients, encrypted, EzyTransportType.TCP);
    }

    @Test
    public void makeSureVisibilityGetExecutorServiceTest() {
        // given
        EzyAppContext appContext = new EzySimpleAppContext();

        // when
        ScheduledExecutorService actual = appContext.getExecutorService();

        // then
        Asserts.assertNull(actual);
    }
}
