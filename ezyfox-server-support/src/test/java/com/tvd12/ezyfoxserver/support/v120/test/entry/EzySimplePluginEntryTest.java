package com.tvd12.ezyfoxserver.support.v120.test.entry;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyPackagesToScanAware;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;
import com.tvd12.test.assertion.Asserts;
import lombok.Setter;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzySimplePluginEntryTest {
	
	@Test
	public void scanPackages() {
		// given
		EzyPluginContext pluginContext = mock(EzyPluginContext.class);
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		EzyZoneContext zoneContext = mock(EzyZoneContext.class);
		EzyServerContext serverContext = mock(EzyServerContext.class);
		EzyPluginSetup pluginSetup = mock(EzyPluginSetup.class);
		
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
				"com.tvd12.ezyfoxserver.support.v120.test.entry"
		);
		
		Asserts.assertEquals(expectedPackages, mongoConfig.packagesToScan);
		
		Singleton singleton = (Singleton) beanContext.getBean(Singleton.class);
		Asserts.assertNotNull(singleton);
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
	
}
