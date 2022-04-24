package com.tvd12.ezyfoxserver.testing.interceptor;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.exception.EzyNotAuthorizedException;
import com.tvd12.ezyfoxserver.interceptor.EzyServerUserInterceptor;
import com.tvd12.ezyfoxserver.request.EzyRequestAppRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;
import org.testng.annotations.Test;

public class EzyServerUserInterceptorTest extends BaseCoreTest {

    @Test(expectedExceptions = {EzyNotAuthorizedException.class})
    public void test() throws Exception {
        EzyServerContext context = newServerContext();
        EzyServerUserInterceptor<EzyArray> interceptor =
            new EzyServerUserInterceptor<>();
        EzyRequestAppRequest request = new EzySimpleRequestAppRequest();
        interceptor.intercept(context, request);
    }

    @Test
    public void test1() throws Exception {
        EzyServerContext context = newServerContext();
        EzyServerUserInterceptor<EzyArray> interceptor =
            new EzyServerUserInterceptor<>();
        EzySimpleRequestAppRequest request = new EzySimpleRequestAppRequest();
        request.setUser(new MyTestUser());
        interceptor.intercept(context, request);
    }

}
