package com.tvd12.ezyfoxserver.testing.setting;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class EzySimpleAppSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        setting.setMaxUsers(1);
        setting.setName("hello");
        setting.setHomePath("abc");
        setting.setEntryLoader(TestAppEntryLoader.class);
        System.out.println(setting.getLocation());
        assertEquals(setting.getEntryLoader(), TestAppEntryLoader.class.getName());
    }
    
    @Test
    public void configFileIsNull() {
    	// given
    	EzySimpleAppSetting sut = new EzySimpleAppSetting();
    	sut.setConfigFile(null);
    	
    	// when
    	String configFile = sut.getConfigFile(true);
    	
    	// then
    	Asserts.assertNull(configFile);
    	System.out.println(sut.toMap());
    }
    
    @Test
    public void getParentFolderWithEntries() {
        // given
        EzySimpleAppSetting sut = new EzySimpleAppSetting();
        sut.setName("test");
        sut.setFolder("test");
        sut.setHomePath("test-data");
        sut.setConfigFile("config.properties");
        
        // when
        String configFile = sut.getConfigFile();
        
        // then
        Asserts.assertEquals("test-data/apps/entries/test/config.properties", configFile);
    }
    
    @Test
    public void getParentFolderNoEntries() {
        // given
        EzySimpleAppSetting sut = new EzySimpleAppSetting();
        sut.setName("test");
        sut.setFolder("test");
        sut.setHomePath("test-data1");
        sut.setConfigFile("config.properties");
        
        // when
        String configFile = sut.getConfigFile();
        
        // then
        Asserts.assertEquals("test-data1/apps/test/config.properties", configFile);
    }
    
    public static class TestAppEntryLoader implements EzyAppEntryLoader {

        @Override
        public EzyAppEntry load() throws Exception {
            return null;
        }
        
    }
}
