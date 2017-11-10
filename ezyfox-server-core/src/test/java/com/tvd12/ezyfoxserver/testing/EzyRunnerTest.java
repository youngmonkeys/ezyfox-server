package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.test.base.BaseTest;

public class EzyRunnerTest extends BaseTest {

	@Test(expectedExceptions = {IllegalStateException.class})
	public void test() throws Exception {
		EzyRunner runner = new MyTestRunner();
		runner.run(new String[0]);
	}
	
	@Test
	public void test1() throws Exception {
	    EzyRunner runner = new MyTestRunner();
        runner.run(new String[] {"test-data/settings/config.properties"});
	}
	
}
