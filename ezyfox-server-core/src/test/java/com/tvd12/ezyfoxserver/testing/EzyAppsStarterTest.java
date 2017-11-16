package com.tvd12.ezyfoxserver.testing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.EzyAppsStarter;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.test.base.BaseTest;

public class EzyAppsStarterTest extends BaseTest {

    @Test
    public void test1() {
        Map<String, EzyAppClassLoader> loaders = new ConcurrentHashMap<>();
        EzyAppsStarter starter = new EzyAppsStarter.Builder() {
            @Override
            public EzyAppsStarter build() {
                return new EzyAppsStarter(this) {
                    
                    public EzyAppEntryLoader newAppEntryLoader(String appName) throws Exception {
                        throw new RuntimeException();
                    };
                    
                    public java.util.Set<String> getAppNames() {
                        return Sets.newHashSet("test");
                    };
                };
            }
        }
        .appClassLoaders(loaders)
        .build();
        starter.start();
    }
    
    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test2() {
        Map<String, EzyAppClassLoader> loaders = new ConcurrentHashMap<>();
        EzyAppsStarter starter = new EzyAppsStarter.Builder()
        .appClassLoaders(loaders)
        .build();
        starter.getClassLoader("zzz");
    }
}
