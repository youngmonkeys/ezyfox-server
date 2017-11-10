package com.tvd12.ezyfoxserver.testing.service;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;

public class EzyResponseSerializerTest {

    @Test
    public void test() {
        ResponseSerializer ser = new ResponseSerializer();
        ser.serializeToObject(null);
    }
    
    public static class ResponseSerializer implements EzyResponseSerializer {
        @Override
        public <T> T serialize(EzyResponse response, Class<T> outType) {
            return null;
        }
    }
    
}
