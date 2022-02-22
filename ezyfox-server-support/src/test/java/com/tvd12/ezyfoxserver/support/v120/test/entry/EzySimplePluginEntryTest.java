package com.tvd12.ezyfoxserver.support.v120.test.entry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPackagesToScanAware;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.support.annotation.EzyDisallowRequest;
import com.tvd12.ezyfoxserver.support.constant.EzySupportConstants;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;
import com.tvd12.test.assertion.Asserts;

import lombok.Setter;

public class EzySimplePluginEntryTest {
	
	@Test
	public void scanPackages() {
		// given
		EzyPluginContext pluginContext = mock(EzyPluginContext.class);
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		EzyZoneContext zoneContext = mock(EzyZoneContext.class);
		EzyServerContext serverContext = mock(EzyServerContext.class);
		EzyPluginSetup pluginSetup = mock(EzyPluginSetup.class);

		EzyPlugin plugin = mock(EzyPlugin.class);
		when(pluginContext.getPlugin()).thenReturn(plugin);
		
		EzyPluginSetting pluginSetting = mock(EzyPluginSetting.class);
		when(plugin.getSetting()).thenReturn(pluginSetting);
		
		InteralPluginEngtry sut = new InteralPluginEngtry();
		
		// when
		when(pluginContext.get(ScheduledExecutorService.class)).thenReturn(executorService);
		when(pluginContext.getParent()).thenReturn(zoneContext);
		when(zoneContext.getParent()).thenReturn(serverContext);
		when(pluginContext.get(EzyPluginSetup.class)).thenReturn(pluginSetup);
		
		sut.config(pluginContext);
		
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
	
	@Test
	public void notAllowRequestTest() {
		// given
		EzyPluginContext pluginContext = mock(EzyPluginContext.class);
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		EzyZoneContext zoneContext = mock(EzyZoneContext.class);
		EzyServerContext serverContext = mock(EzyServerContext.class);
		EzyPluginSetup pluginSetup = mock(EzyPluginSetup.class);
		
		EzyPlugin plugin = mock(EzyPlugin.class);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        
        EzyPluginSetting pluginSetting = mock(EzyPluginSetting.class);
        when(plugin.getSetting()).thenReturn(pluginSetting);
		
		NotAllowRequestEngtry sut = new NotAllowRequestEngtry();
		
		// when
		when(pluginContext.get(ScheduledExecutorService.class)).thenReturn(executorService);
		when(pluginContext.getParent()).thenReturn(zoneContext);
		when(zoneContext.getParent()).thenReturn(serverContext);
		when(pluginContext.get(EzyPluginSetup.class)).thenReturn(pluginSetup);
		
		sut.config(pluginContext);
		
		// then
		verify(pluginContext, times(0)).get(EzyPluginSetup.class);
	}
	
	@Test
	public void disallowRequestTest() {
		// given
		EzyPluginContext pluginContext = mock(EzyPluginContext.class);
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		EzyZoneContext zoneContext = mock(EzyZoneContext.class);
		EzyServerContext serverContext = mock(EzyServerContext.class);
		EzyPluginSetup pluginSetup = mock(EzyPluginSetup.class);
		
		EzyPlugin plugin = mock(EzyPlugin.class);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        
        EzyPluginSetting pluginSetting = mock(EzyPluginSetting.class);
        when(plugin.getSetting()).thenReturn(pluginSetting);
		
		DisAllowRequestEngtry sut = new DisAllowRequestEngtry();
		
		// when
		when(pluginContext.get(ScheduledExecutorService.class)).thenReturn(executorService);
		when(pluginContext.getParent()).thenReturn(zoneContext);
		when(zoneContext.getParent()).thenReturn(serverContext);
		when(pluginContext.get(EzyPluginSetup.class)).thenReturn(pluginSetup);
		
		sut.config(pluginContext);
		
		// then
		verify(pluginContext, times(0)).get(EzyPluginSetup.class);
	}
	
	@EzySingleton
	public static class Singleton {
	}
	
	@Setter
	@EzyConfigurationBefore
	public static class MongoConfig implements EzyPackagesToScanAware {
		public Set<String> packagesToScan;
	}
	
	private static class InteralPluginEngtry extends EzySimplePluginEntry {
		public EzyBeanContext beanContext;
		
		@Override
		protected String[] getScanablePackages() {
			return new String[]{"com.tvd12.ezyfoxserver.support.v120.test.entry"};
		}
		
		@Override
		protected void postConfig(EzyPluginContext ctx, EzyBeanContext beanContext) {
			this.beanContext = beanContext;
		}
	}
	
	private static class NotAllowRequestEngtry extends EzySimplePluginEntry {
		@Override
		protected boolean allowRequest() {
			return false;
		}
	}
	
	@EzyDisallowRequest
	private static class DisAllowRequestEngtry extends EzySimplePluginEntry {
	}
}
