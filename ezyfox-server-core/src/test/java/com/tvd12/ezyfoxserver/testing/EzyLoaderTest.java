package com.tvd12.ezyfoxserver.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyLoader;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager.Builder;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodUtil;

public class EzyLoaderTest extends BaseCoreTest {
    
    @Test
    public void test() throws Exception {
        EzySimpleServer server = newServer();
        
        EzyConfig config = server.getConfig();
        assertEquals(config.getEzyfoxHome(), "test-data");
        assertEquals(config.getLoggerConfigFile(), "logback.groovy");
    }

    @Test
    public void newAppClassLoaderNotEnableClassLoader() {
    	// given
    	InternalLoader sut = new InternalLoader();
    	FieldUtil.setFieldValue(sut, "classLoader", Thread.currentThread().getContextClassLoader());
    	
    	EzyConfig config = mock(EzyConfig.class);
    	when(config.isEnableAppClassLoader()).thenReturn(false);
    	FieldUtil.setFieldValue(sut, "config", config);
    	
    	// when
    	ClassLoader classLoader = MethodUtil.invokeMethod("newAppClassLoader", sut, new File(""));
    	
    	// then
    	Asserts.assertEquals(Thread.currentThread().getContextClassLoader(), classLoader);
    }
    
    @Test
    public void getEntryFoldersEmptyTest() {
    	// given
    	InternalLoader sut = new InternalLoader();
    	
    	EzyConfig config = mock(EzyConfig.class);
    	when(config.isEnableAppClassLoader()).thenReturn(false);
    	FieldUtil.setFieldValue(sut, "config", config);
    	
    	// when
    	File[] folders = MethodUtil.invokeMethod("getEntryFolders", sut);
    	
    	// then
    	Asserts.assertEquals(new File[0], folders);
    }
    
    private static class InternalLoader extends EzyLoader {

		@SuppressWarnings("rawtypes")
		@Override
		protected Builder createSessionManagerBuilder(EzySettings settings) {
			return null;
		}
    }
}
