package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.EzyAppsStarter;
import com.tvd12.ezyfoxserver.EzySimpleApplication;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EzyAppsStarterTest extends BaseTest {

    @Test
    public void test1() {
        Map<String, ClassLoader> loaders = new ConcurrentHashMap<>();
        EzyAppsStarter starter = new EzyAppsStarter.Builder() {
            @Override
            public EzyAppsStarter build() {
                return new EzyAppsStarter(this) {

                    public EzyAppEntryLoader newAppEntryLoader(String appName) throws Exception {
                        throw new RuntimeException();
                    }

                    ;

                    public java.util.Set<String> getAppNames() {
                        return Sets.newHashSet("test");
                    }

                    ;
                };
            }
        }
            .zoneContext(EzyZoneContextsTest.newDefaultZoneContext())
            .appClassLoaders(loaders)
            .build();
        starter.start();
    }

    @Test
    public void test2() {
        Map<String, ClassLoader> loaders = new ConcurrentHashMap<>();
        EzySimpleZoneContext zoneContext = EzyZoneContextsTest.newDefaultZoneContext();
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("abc");
        app.setSetting(appSetting);
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        appContext.setApp(app);
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
        appsSetting.setItem(appSetting);
        zoneSetting.setApplications(appsSetting);
        zoneContext.addAppContext(appSetting, appContext);
        EzyAppsStarter starter = new EzyAppsStarter.Builder()
            .zoneContext(zoneContext)
            .appClassLoaders(loaders)
            .build();
        starter.start();
    }

    @Test
    public void getClassLoaderErrorCaseTest() {
        Map<String, ClassLoader> loaders = new ConcurrentHashMap<>();
        EzySimpleZoneContext zoneContext = EzyZoneContextsTest.newDefaultZoneContext();
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("abc");
        app.setSetting(appSetting);
        EzySimpleAppContext appContext = new EzySimpleAppContext();
        appContext.setApp(app);
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
        appsSetting.setItem(appSetting);
        zoneSetting.setApplications(appsSetting);
        zoneContext.addAppContext(appSetting, appContext);
        EzyAppsStarter starter = new EzyAppsStarter.Builder()
            .zoneContext(zoneContext)
            .appClassLoaders(loaders)
            .build();
        try {
            MethodInvoker.create()
                .object(starter)
                .method("getAppClassLoader")
                .param("abc")
                .param("hello")
                .invoke();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            assert e.getCause().getCause() instanceof IllegalArgumentException;
        }
    }

    @Test
    public void newAppEntryLoaderArgsNotNullTest() {
        // given
        Map<String, ClassLoader> loaders = new ConcurrentHashMap<>();
        EzySimpleZoneContext zoneContext = EzyZoneContextsTest.newDefaultZoneContext();
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("abc");
        appSetting.setEntryLoader(InternalAppEntryLoader.class);
        appSetting.setEntryLoaderArgs(new String[]{"Hello"});
        app.setSetting(appSetting);

        EzySimpleAppContext appContext = new EzySimpleAppContext();
        appContext.setApp(app);


        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
        appsSetting.setItem(appSetting);
        zoneSetting.setApplications(appsSetting);
        zoneContext.addAppContext(appSetting, appContext);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);
        zoneContext.setZone(zone);

        EzyAppsStarter starter = new EzyAppsStarter.Builder()
            .zoneContext(zoneContext)
            .appClassLoaders(loaders)
            .enableAppClassLoader(false)
            .classLoader(Thread.currentThread().getContextClassLoader())
            .build();

        // when
        starter.start();

        // then
        Asserts.assertNotNull(app.getEntry());
    }

    @Test
    public void newAppEntryLoaderClassLoaderIsNull() {
        // given
        Map<String, ClassLoader> loaders = new ConcurrentHashMap<>();
        EzySimpleZoneContext zoneContext = EzyZoneContextsTest.newDefaultZoneContext();
        EzySimpleApplication app = new EzySimpleApplication();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("abc");
        app.setSetting(appSetting);

        EzySimpleAppContext appContext = new EzySimpleAppContext();
        appContext.setApp(app);


        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimpleAppsSetting appsSetting = new EzySimpleAppsSetting();
        appsSetting.setItem(appSetting);
        zoneSetting.setApplications(appsSetting);
        zoneContext.addAppContext(appSetting, appContext);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);
        zoneContext.setZone(zone);

        EzyAppsStarter starter = new EzyAppsStarter.Builder()
            .zoneContext(zoneContext)
            .appClassLoaders(loaders)
            .enableAppClassLoader(true)
            .build();

        // when
        starter.start();

        // then
        Asserts.assertNull(app.getEntry());
    }

    public static class InternalAppEntryLoader implements EzyAppEntryLoader {

        public InternalAppEntryLoader(String arg) {
            System.out.println(arg);
        }

        @Override
        public EzyAppEntry load() throws Exception {
            return new EzyAppEntry() {};
        }
    }
}
