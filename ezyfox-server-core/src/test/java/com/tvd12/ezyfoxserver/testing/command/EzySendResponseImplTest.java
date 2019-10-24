package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.command.impl.EzySendResponseImpl;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.response.EzySimpleResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;

import static org.mockito.Mockito.*;

public class EzySendResponseImplTest {

    @Test
    public void responseSuccessCaseTest() {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyResponseApi responseApi = spy(EzyAbstractResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.APP_REQUEST);
        EzySession recipient = spy(EzyAbstractSession.class);
        cmd.execute(response, recipient);
        cmd.execute(response, Lists.newArrayList(recipient));
    }
    
    @Test
    public void responseMultiExceptionCase() throws Exception {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        doThrow(new IllegalArgumentException()).when(responseApi).response(any(EzyPackage.class), anyBoolean());
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.APP_REQUEST);
        EzySession recipient = spy(EzyAbstractSession.class);
        cmd.execute(response, Lists.newArrayList(recipient));
    }
    
}
