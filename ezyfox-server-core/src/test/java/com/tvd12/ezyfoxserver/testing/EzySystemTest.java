package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySystem;
import com.tvd12.test.base.BaseTest;

public class EzySystemTest extends BaseTest {

    @Test
    public void test() {
        EzySystem.getEnv().setProperty("1", "a");
        assert EzySystem.getEnv().getProperty("1").equals("a");
    }
    
    @Override
    public Class<?> getTestClass() {
        return EzySystem.class;
    }
    
}
