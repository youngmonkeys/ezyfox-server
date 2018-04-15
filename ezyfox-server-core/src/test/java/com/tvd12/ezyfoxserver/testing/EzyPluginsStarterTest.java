package com.tvd12.ezyfoxserver.testing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.EzyAppsStarter;
import com.tvd12.ezyfoxserver.EzyPluginsStarter;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
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
        Map<String, EzyAppClassLoader> loaders = new ConcurrentHashMap<>();
        EzyAppsStarter starter = new EzyAppsStarter.Builder()
                .zoneContext(EzyZoneContextsTest.newDefaultZoneContext())
                .appClassLoaders(loaders)
                .build();
        starter.start();
    }
}
