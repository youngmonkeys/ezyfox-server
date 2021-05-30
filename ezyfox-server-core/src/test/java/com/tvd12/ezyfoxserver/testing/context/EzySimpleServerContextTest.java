package com.tvd12.ezyfoxserver.testing.context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.command.EzyBroadcastEvent;
import com.tvd12.ezyfoxserver.command.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.exception.EzyZoneNotFoundException;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleServerContextTest extends BaseCoreTest {

    private EzySimpleServerContext context;
    
    public EzySimpleServerContextTest() {
        super();
        context = (EzySimpleServerContext) newServerContext();
    }
    
    @Test
    public void test() {
        EzySimpleServerContext ctx = ((EzySimpleServerContext)context);
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        EzySimpleApplication app = new EzySimpleApplication();
        EzyZoneContext zoneContext = ctx.getZoneContexts().get(0);
        appContext.setParent(zoneContext);
        appContext.setApp(app);
        appContext.init();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("abcxyz");
        ctx.addAppContext(appSetting, appContext);
        assert ctx.getAppContext(appSetting.getId()) != null;
        
        context.setProperty("test.1", "abc");
        assert context.getProperty("test.1") != null;
        assert appContext.getProperty("test.1") == null;
        
        appContext.setProperty("test.2", "abc");
        assert context.getProperty("test.2") == null;
        assert appContext.getProperty("test.2") != null;
        
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("plugin.1");
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        EzySimplePlugin plugin = new EzySimplePlugin();
        pluginContext.setParent(zoneContext);
        pluginContext.setPlugin(plugin);
        pluginContext.init();
        ctx.addPluginContext(pluginSetting, pluginContext);
        assert ctx.getPluginContext(pluginSetting.getId()) != null;
        
        context.setProperty("test.1", "abc");
        assert context.getProperty("test.1") != null;
        assert pluginContext.getProperty("test.1") == null;
        
        pluginContext.setProperty("test.2", "abc");
        assert context.getProperty("test.2") == null;
        assert pluginContext.getProperty("test.2") != null;
        
        assert context.get(EzyBroadcastEvent.class) != null;
        context.addCommand(ExCommand.class, () -> new ExCommand());
        assert context.cmd(ExCommand.class) != null;
        try {
            context.cmd(Void.class);
        }
        catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        
        EzyServerReadyEvent serverReadyEvent = new EzySimpleServerReadyEvent();
        context.broadcast(EzyEventType.SERVER_READY, serverReadyEvent, true);
        
        EzySimpleServer server = (EzySimpleServer)context.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        server.setStreamingApi(mock(EzyStreamingApi.class));
        
        EzyResponse response = mock(EzyResponse.class);
        EzySession recipient = spy(EzyAbstractSession.class);
        context.send(response, recipient, false);
        context.send(response, Lists.newArrayList(recipient), false, true);
        context.stream(new byte[0], recipient);
        context.stream(new byte[0], Lists.newArrayList(recipient));
        
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        user.addSession(recipient);
        context.send(response, user, false);
        context.send(response, Lists.newArrayList(user), false);
        
        assert context.getZoneContext(zoneContext.getZone().getSetting().getId()) != null;
        try {
            context.getZoneContext(-1);
        }
        catch (Exception e) {
            assert e instanceof EzyZoneNotFoundException;
        }
        
        try {
            context.getZoneContext("not found");
        }
        catch (Exception e) {
            assert e instanceof EzyZoneNotFoundException;
        }
        
        context.destroy();
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test1() {
        context.get(Class.class);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test2() {
        context.getAppContext(-100);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test3() {
        context.getPluginContext(-1000);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test4() {
        EzyZoneContext zoneContext = context.getZoneContext("example");
        zoneContext.getAppContext("noone");
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test5() {
        EzyZoneContext zoneContext = context.getZoneContext("example");
        zoneContext.getPluginContext("noone");
    }
    
    public static class ExCommand implements EzyCommand<Boolean> {

        @Override
        public Boolean execute() {
            return Boolean.TRUE;
        }
        
    }
}
