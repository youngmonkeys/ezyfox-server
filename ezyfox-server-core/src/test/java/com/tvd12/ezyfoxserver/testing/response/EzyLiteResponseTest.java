package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.response.EzyLiteResponse;
import com.tvd12.test.base.BaseTest;

public class EzyLiteResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyLiteResponse response = (EzyLiteResponse) new MyResponse.Builder()
                .appId(1)
                .command(EzyCommand.APP_ACCESS)
                .data(new Integer(123))
                .build();
        
        assert response.getAppId() == 1;
        assert response.getCommand() == EzyCommand.APP_ACCESS;
        assert response.getData().equals(new Integer(123));
    }
    
 private static class MyResponse extends EzyLiteResponse {
        
        protected MyResponse(Builder builder) {
            super(builder);
        }
        
        public static class Builder extends EzyLiteResponse.Builder<Builder> {
            @Override
            public EzyLiteResponse build() {
                return new MyResponse(this);
            }
        }
        
    }
}
