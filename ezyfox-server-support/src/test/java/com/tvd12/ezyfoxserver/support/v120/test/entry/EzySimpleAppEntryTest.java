package com.tvd12.ezyfoxserver.support.v120.test.entry;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPackagesToScanAware;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyAppSetup;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.support.constant.EzySupportConstants;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.test.assertion.Asserts;
import lombok.Setter;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzySimpleAppEntryTest {

    @Test
    public void scanPackages() {
        // given
        EzyAppContext appContext = mock(EzyAppContext.class);
        ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyApplication application = mock(EzyApplication.class);
        EzyAppUserManager appUserManager = mock(EzyAppUserManager.class);
        EzyAppSetup appSetup = mock(EzyAppSetup.class);

        EzyAppSetting appSetting = mock(EzyAppSetting.class);
        when(application.getSetting()).thenReturn(appSetting);

        InternalAppEntry sut = new InternalAppEntry();

        // when
        when(appContext.get(ScheduledExecutorService.class)).thenReturn(executorService);
        when(appContext.getParent()).thenReturn(zoneContext);
        when(zoneContext.getParent()).thenReturn(serverContext);
        when(appContext.getApp()).thenReturn(application);
        when(application.getUserManager()).thenReturn(appUserManager);
        when(appContext.get(EzyAppSetup.class)).thenReturn(appSetup);

        sut.config(appContext);

        // then
        EzyBeanContext beanContext = sut.beanContext;
        MongoConfig mongoConfig = (MongoConfig) beanContext.getBean(MongoConfig.class);

        Set<String> expectedPackages = Sets.newHashSet(
            EzySupportConstants.DEFAULT_PACKAGE_TO_SCAN,
            "com.tvd12.ezyfoxserver.support.v120.test.entry"
        );

        Asserts.assertEquals(expectedPackages, mongoConfig.packagesToScan);

        Singleton singleton = (Singleton) beanContext.getBean(Singleton.class);
        Asserts.assertNotNull(singleton);
    }

    @EzySingleton
    public static class Singleton {}

    @Setter
    @EzyConfigurationBefore
    public static class MongoConfig implements EzyPackagesToScanAware {
        public Set<String> packagesToScan;
    }

    private static class InternalAppEntry extends EzySimpleAppEntry {
        public EzyBeanContext beanContext;

        @Override
        protected String[] getScanablePackages() {
            return new String[]{"com.tvd12.ezyfoxserver.support.v120.test.entry"};
        }

        @Override
        protected void postConfig(EzyAppContext context, EzyBeanContext beanContext) {
            this.beanContext = beanContext;
        }
    }
}
