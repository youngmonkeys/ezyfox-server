package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class EzyRunnerTest extends BaseTest {

    @Test(expectedExceptions = { IllegalStateException.class })
    public void test() throws Exception {
        EzyRunner runner = new MyTestRunner();
        runner.run(new String[0]);
    }
    
    @Test
    public void testWithNoArg() throws Exception {
        try {
            EzyRunner runner = new MyTestRunner() {
                protected void validateArguments(String[] args) {};
            };
            runner.run(new String[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws Exception {
        try {
            EzyRunner runner = new MyTestRunner();
            runner.run(new String[] {"test-data/settings/config.properties"});
            Asserts.assertNotNull(runner.getServerContext());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
