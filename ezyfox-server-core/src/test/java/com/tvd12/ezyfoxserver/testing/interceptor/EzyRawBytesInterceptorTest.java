package com.tvd12.ezyfoxserver.testing.interceptor;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.exception.EzyNotAuthorizedException;
import com.tvd12.ezyfoxserver.interceptor.EzyRawBytesInterceptor;
import com.tvd12.ezyfoxserver.request.EzySimpleStreamingRequest;
import com.tvd12.ezyfoxserver.request.EzyStreamingRequest;

public class EzyRawBytesInterceptorTest {

    @Test
    public void test() throws Exception {
        EzyRawBytesInterceptor interceptor = new EzyRawBytesInterceptor();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzySimpleStreamingRequest request = new EzySimpleStreamingRequest();
        request.setUser(new EzySimpleUser());
        interceptor.intercept(serverContext, request);
    }
    
    @Test(expectedExceptions = EzyNotAuthorizedException.class)
    public void test2() throws Exception {
        EzyRawBytesInterceptor interceptor = new EzyRawBytesInterceptor();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyStreamingRequest request = new EzySimpleStreamingRequest();
        interceptor.intercept(serverContext, request);
    }
    
}
