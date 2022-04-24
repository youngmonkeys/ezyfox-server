package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.*;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyZonesStarterTest extends BaseTest {

    @Test
    public void normalCaseTest() {
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleZonesSetting zonesSetting = settings.getZones();
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.setName("test");
        zonesSetting.setItem(zoneSetting);

        EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("apps");
        appSetting.setFolder("apps");
        appSetting.setEntryLoader(ExEntryLoader.class.getName());
        appsSetting.setItem(appSetting);
        zoneSetting.setApplications(appsSetting);

        EzySimplePluginsSetting pluginsSetting = new EzySimplePluginsSetting();
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("plugins");
        pluginSetting.setFolder("plugins");
        pluginSetting.setEntryLoader(ExPluginEntryLoader.class.getName());
        pluginsSetting.setItem(pluginSetting);
        zoneSetting.setPlugins(pluginsSetting);

        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        server.setConfig(new EzySimpleConfig());
        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);
        when(serverContext.getZoneContext("test")).thenReturn(zoneContext);

        EzySimpleApplication app = new EzySimpleApplication();
        app.setSetting(appSetting);
        EzyAppContext appContext = mock(EzyAppContext.class);
        when(appContext.getApp()).thenReturn(app);
        when(zoneContext.getAppContext("apps")).thenReturn(appContext);

        EzySimplePlugin plugin = new EzySimplePlugin();
        plugin.setSetting(pluginSetting);
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        when(zoneContext.getPluginContext("plugins")).thenReturn(pluginContext);

        Map<String, ClassLoader> appClassLoaders = new HashMap<>();
        appClassLoaders.put("apps", new EzyAppClassLoader(new File("test-data"), getClass().getClassLoader()));
        server.setAppClassLoaders(appClassLoaders);

        EzyZonesStarter starter = EzyZonesStarter.builder()
            .serverContext(serverContext)
            .build();
        starter.start();
    }

    public static class ExEntryLoader implements EzyAppEntryLoader {

        @Override
        public EzyAppEntry load() throws Exception {
            EzyAppEntry enry = mock(EzyAppEntry.class);
            return enry;
        }

    }

    public static class ExPluginEntryLoader implements EzyPluginEntryLoader {

        @Override
        public EzyPluginEntry load() throws Exception {
            EzyPluginEntry entry = mock(EzyPluginEntry.class);
            return entry;
        }

    }
}
