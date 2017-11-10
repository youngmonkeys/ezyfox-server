package com.tvd12.ezyfoxserver.testing.interceptor;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.exception.NotAuthorizedException;
import com.tvd12.ezyfoxserver.interceptor.EzyServerUserInterceptor;
import com.tvd12.ezyfoxserver.request.EzyRequestAppRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestUser;

public class EzyServerUserInterceptorTest extends BaseCoreTest {

    @Test(expectedExceptions = {NotAuthorizedException.class})
    public void test() throws Exception {
        EzyServerContext context = newServerContext();
        EzyServerUserInterceptor<EzyArray> interceptor = 
                new EzyServerUserInterceptor<>();
        EzyRequestAppRequest request = EzySimpleRequestAppRequest.builder()
                .build();
        interceptor.intercept(context, request);
    }
    
    @Test
    public void test1() throws Exception {
        EzyServerContext context = newServerContext();
        EzyServerUserInterceptor<EzyArray> interceptor = 
                new EzyServerUserInterceptor<>();
        EzyRequestAppRequest request = EzySimpleRequestAppRequest.builder()
                .user(new MyTestUser())
                .build();
        interceptor.intercept(context, request);
    }
    
}
