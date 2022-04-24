package com.tvd12.ezyfoxserver.testing.interceptor;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyInterceptorTest extends BaseCoreTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Exception {
        EzyServerContext context = newServerContext();
        EzyInterceptor.ALWAYS_PASS.intercept(context, null);

    }

}
