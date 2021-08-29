package com.tvd12.ezyfoxserver.testing.command;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.command.impl.EzySendResponseImpl;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.response.EzySimpleResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;

public class EzySendResponseImplTest {

    @Test
    public void responseOneSuccessCaseTest() {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyResponseApi responseApi = spy(EzyAbstractResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.APP_REQUEST);
        EzySession recipient = spy(EzyAbstractSession.class);
        cmd.execute(response, recipient, false, false, EzyTransportType.TCP);
    }
    
    @Test
    public void responseOneSuccessButNotDebug() throws Exception {
    	// given
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(false);
        EzyResponseApi responseApi = spy(EzyAbstractResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.APP_REQUEST);
        EzySession recipient = spy(EzyAbstractSession.class);
        
        // when
        cmd.execute(response, recipient, false, false, EzyTransportType.TCP);
        
        // then
        verify(responseApi, times(1)).response(any(EzyPackage.class), anyBoolean());
    }
    
    @Test
    public void responseOneSuccessButIsPong() throws Exception {
    	// given
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyResponseApi responseApi = spy(EzyAbstractResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.PONG);
        EzySession recipient = spy(EzyAbstractSession.class);
        
        // when
        cmd.execute(response, recipient, false, false, EzyTransportType.TCP);
        
        // then
        verify(responseApi, times(1)).response(any(EzyPackage.class), anyBoolean());
    }
    
    @Test
    public void responseOneExceptionCase() throws Exception {
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
        cmd.execute(response, recipient, false, false, EzyTransportType.TCP);
    }
    
    @Test
    public void responseMultiSuccessCase() throws Exception {
    	// when
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.APP_REQUEST);
        EzySession recipient = spy(EzyAbstractSession.class);
        List<EzySession> recipients = Arrays.asList(recipient);
        
        // when
        cmd.execute(response, recipients, false, false, EzyTransportType.TCP);
        
        // then
        verify(responseApi, times(1)).response(any(EzyPackage.class), anyBoolean());
    }
    
    @Test
    public void responseMultiSuccessCaseButNotDebug() throws Exception {
    	// when
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(false);
        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.APP_REQUEST);
        EzySession recipient = spy(EzyAbstractSession.class);
        List<EzySession> recipients = Arrays.asList(recipient);
        
        // when
        cmd.execute(response, recipients, false, false, EzyTransportType.TCP);
        
        // then
        verify(responseApi, times(1)).response(any(EzyPackage.class), anyBoolean());
    }
    
    @Test
    public void responseMultiSuccessCaseButIsPong() throws Exception {
    	// when
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setDebug(true);
        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        EzySimpleServer server = new EzySimpleServer();
        server.setResponseApi(responseApi);
        server.setSettings(settings);
        EzySendResponseImpl cmd = new EzySendResponseImpl(server);
        EzyResponse response = new EzySimpleResponse(EzyCommand.PONG);
        EzySession recipient = spy(EzyAbstractSession.class);
        List<EzySession> recipients = Arrays.asList(recipient);
        
        // when
        cmd.execute(response, recipients, false, false, EzyTransportType.TCP);
        
        // then
        verify(responseApi, times(1)).response(any(EzyPackage.class), anyBoolean());
    }
    
    @Test
    public void responseMultiErrorTest() throws Exception {
    	// given
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
        List<EzySession> recipients = Arrays.asList(recipient);
        
        // when
        cmd.execute(response, recipients, false, false, EzyTransportType.TCP);
        
        // then
        verify(responseApi, times(1)).response(any(EzyPackage.class), anyBoolean());
    }
    
}
