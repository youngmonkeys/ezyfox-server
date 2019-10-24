package com.tvd12.ezyfoxserver.testing.command;

import static org.mockito.Mockito.*;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyAbstractStreamingApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.command.impl.EzyStreamBytesImpl;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyBytesPackage;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;

public class EzyStreamBytesImplTest {

    @Test
    public void normalCaseTest() {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyStreamingApi streamingApi = spy(EzyAbstractStreamingApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setStreamingApi(streamingApi);
        server.setSettings(settings);
        EzyStreamBytesImpl cmd = new EzyStreamBytesImpl(server);
        
        EzySession recipient = spy(EzyAbstractSession.class);
        cmd.execute(new byte[] {1, 2, 3}, recipient);
        cmd.execute(new byte[] {1, 2, 3}, Lists.newArrayList(recipient));
    }
    
    @Test
    public void exceptionCaseTest() throws Exception {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyStreamingApi streamingApi = spy(EzyStreamingApi.class);
        doThrow(new IllegalArgumentException()).when(streamingApi).response(any(EzyBytesPackage.class));
        EzySimpleServer server = new EzySimpleServer();
        server.setStreamingApi(streamingApi);
        server.setSettings(settings);
        EzyStreamBytesImpl cmd = new EzyStreamBytesImpl(server);
        
        EzySession recipient = spy(EzyAbstractSession.class);
        cmd.execute(new byte[] {1, 2, 3}, recipient);
        cmd.execute(new byte[] {1, 2, 3}, Lists.newArrayList(recipient));
    }
    
}
