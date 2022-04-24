package com.tvd12.ezyfoxserver.testing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.EzyAppsStarter;
import com.tvd12.ezyfoxserver.EzyPluginsStarter;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class EzyPluginsStarterTest extends BaseTest {

    @Test
    public void test1() {
        EzyPluginsStarter starter = new EzyPluginsStarter.Builder() {
            @Override
            public EzyPluginsStarter build() {
                return new EzyPluginsStarter(this) {
                    public java.util.Set<String> getPluginNames() {
                        return Sets.newHashSet("test");
                    };
                    
                    public EzyPluginEntryLoader newPluginEntryLoader(String pluginName)
                            throws Exception {
                        throw new Exception();
                    };
                };
            }
        }
        .zoneContext(EzyZoneContextsTest.newDefaultZoneContext())
        .build();
        starter.start();
    }
    
    @Test
    public void test2() {
        Map<String, ClassLoader> loaders = new ConcurrentHashMap<>();
        EzyAppsStarter starter = new EzyAppsStarter.Builder()
                .zoneContext(EzyZoneContextsTest.newDefaultZoneContext())
                .appClassLoaders(loaders)
                .build();
        starter.start();
    }
    
    @Test
    public void newAppEntryLoaderArgsNotNullTest() {
        // given
        EzySimpleZoneContext zoneContext = EzyZoneContextsTest.newDefaultZoneContext();
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("abc");
        pluginSetting.setEntryLoader(InternalPluginEntryLoader.class);
        pluginSetting.setEntryLoaderArgs(new String[] { "Hello" });
        plugin.setSetting(pluginSetting);
        
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        pluginContext.setPlugin(plugin);
        
        
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimplePluginsSetting pluginsSetting = new EzySimplePluginsSetting();
        pluginsSetting.setItem(pluginSetting);
        zoneSetting.setPlugins(pluginsSetting);
        zoneContext.addPluginContext(pluginSetting, pluginContext);
        
        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);
        zoneContext.setZone(zone);
        
        EzyPluginsStarter starter = new EzyPluginsStarter.Builder()
                .zoneContext(zoneContext)
                .build();
        
        // when
        starter.start();
        
        // then
        Asserts.assertNotNull(plugin.getEntry());
    }
    
    public static class InternalPluginEntryLoader implements EzyPluginEntryLoader {

        public InternalPluginEntryLoader(String arg) {
            System.out.println(arg);
        }

        @Override
        public EzyPluginEntry load() throws Exception {
            return new EzyPluginEntry() {};
        }
    }
}
