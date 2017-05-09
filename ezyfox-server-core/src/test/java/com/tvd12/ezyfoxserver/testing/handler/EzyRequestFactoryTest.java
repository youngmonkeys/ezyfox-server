package com.tvd12.ezyfoxserver.testing.handler;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.handler.EzyRequestFactory;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleAccessAppParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleHandShakeParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleLoginParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyRequestFactoryTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyUser user = newUser();
        EzySession session = newSession();
        EzyRequestFactory factory = EzyRequestFactory.builder()
                .userSupplier(() -> user)
                .sessionSupplier(() -> session)
                .build();
        factory.newRequest(EzyCommand.LOGIN, EzySimpleLoginParams.builder().build());
        factory.newRequest(EzyCommand.APP_ACCESS, EzySimpleAccessAppParams.builder().build());
        factory.newRequest(EzyCommand.APP_REQUEST, EzySimpleRequestAppParams.builder().build());
        factory.newRequest(EzyCommand.HANDSHAKE, EzySimpleHandShakeParams.builder().build());
    }
    
}
