package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfoxserver.api.EzyProxyStreamingApi;
import com.tvd12.ezyfoxserver.response.EzyBytesPackage;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzyProxyStreamingApiTest extends BaseTest {

    @Test
    public void test() throws Exception {
        EzyProxyStreamingApi api = new EzyProxyStreamingApi();
        EzyBytesPackage pack = mock(EzyBytesPackage.class);
        api.response(pack);
    }

}
