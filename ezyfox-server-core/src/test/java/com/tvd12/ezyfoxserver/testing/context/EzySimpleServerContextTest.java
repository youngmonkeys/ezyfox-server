package com.tvd12.ezyfoxserver.testing.context;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleServerContextTest extends BaseCoreTest {

    private EzyServerContext context;
    
    public EzySimpleServerContextTest() {
        super();
        context = newServerContext();
        EzySimpleServerContext ctx = ((EzySimpleServerContext)context);
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("abcxyz");
        ctx.addAppContext(appSetting, appContext);
        
        context.setProperty("test.1", "abc");
        assert context.getProperty("test.1") != null;
        assert appContext.getProperty("test.1") == null;
        
        appContext.setProperty("test.2", "abc");
        assert context.getProperty("test.2") == null;
        assert appContext.getProperty("test.2") != null;
        
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("plugin.1");
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        ctx.addPluginContext(pluginSetting, pluginContext);
        
        context.setProperty("test.1", "abc");
        assert context.getProperty("test.1") != null;
        assert pluginContext.getProperty("test.1") == null;
        
        pluginContext.setProperty("test.2", "abc");
        assert context.getProperty("test.2") == null;
        assert pluginContext.getProperty("test.2") != null;
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
}
