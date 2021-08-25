package com.tvd12.ezyfoxserver.testing.config;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.test.assertion.Asserts;

public class EzyConfigTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleConfig config = new EzySimpleConfig();
        config.setEzyfoxHome("home");
        config.setLoggerConfigFile("abc");
        assert config.toString() != null;
    }
    
    @Test
    public void getEzyfoxHomeTest() {
    	// given
    	EzySimpleConfig sut = EzySimpleConfig.defaultConfig();
    	sut.setEzyfoxHome(null);
    	sut.setLoggerConfigFile(null);
    	
    	// when
    	String home = sut.getEzyfoxHome();
    	
    	// then
    	Asserts.assertEquals("", home);
    	System.out.println(sut.toMap());
    }
    
}
