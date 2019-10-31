package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.response.EzyPongResponse;
import com.tvd12.test.base.BaseTest;

public class EzyPongResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyPongResponse response = EzyPongResponse.getInstance();
        assert response.serialize() != null;
        assert response.getCommand() == EzyCommand.PONG;
        response.release();
    }
    
}
