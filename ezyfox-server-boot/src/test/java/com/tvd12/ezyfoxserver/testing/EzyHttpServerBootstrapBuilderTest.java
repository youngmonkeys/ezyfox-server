package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyHttpServerBootstrapBuilderTest extends BaseTest {

    @Test
    public void test() {
        new ExEzyHttpServerBootstrapBuilder();
    }

    public static class ExEzyHttpServerBootstrapBuilder
        extends EzyHttpServerBootstrapBuilder {

        @Override
        protected EzyServerBootstrap newServerBootstrap() {
            return null;
        }
    }
}
