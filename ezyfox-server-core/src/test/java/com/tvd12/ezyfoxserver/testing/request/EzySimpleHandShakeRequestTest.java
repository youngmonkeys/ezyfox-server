package com.tvd12.ezyfoxserver.testing.request;

import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfox.util.EzyEntityObjects;
import com.tvd12.ezyfoxserver.request.EzySimpleHandShakeParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EzySimpleHandShakeRequestTest extends BaseCoreTest {

    @Test
    public void deserializeTest() {
        // given
        String clientId = RandomUtil.randomShortAlphabetString();
        byte[] clientKey = RandomUtil.randomShortByteArray();
        String clientType = RandomUtil.randomShortAlphabetString();
        String clientVersion = RandomUtil.randomShortAlphabetString();
        String reconnectToken = RandomUtil.randomShortAlphabetString();
        boolean enableEncryption = RandomUtil.randomBoolean();
        Object data = EzyEntityObjects.newObject("hello", "world");

        // when
        EzySimpleHandShakeParams sut = new EzySimpleHandShakeParams();
        sut.deserialize(
            EzyEntityArrays.newArray(
                clientId,
                clientKey,
                clientType,
                clientVersion,
                enableEncryption,
                reconnectToken,
                data
            )
        );

        // then
        Asserts.assertEquals(sut.getClientId(), clientId);
        Asserts.assertEquals(sut.getClientKey(), clientKey);
        Asserts.assertEquals(sut.getClientType(), clientType);
        Asserts.assertEquals(sut.getClientVersion(), clientVersion);
        Asserts.assertEquals(sut.getReconnectToken(), reconnectToken);
        Asserts.assertEquals(sut.isEnableEncryption(), enableEncryption);
        Asserts.assertEquals(sut.getData(), data);
    }

    @Test
    public void deserializeWithoutDataTest() {
        // given
        String clientId = RandomUtil.randomShortAlphabetString();
        byte[] clientKey = RandomUtil.randomShortByteArray();
        String clientType = RandomUtil.randomShortAlphabetString();
        String clientVersion = RandomUtil.randomShortAlphabetString();
        String reconnectToken = RandomUtil.randomShortAlphabetString();
        boolean enableEncryption = RandomUtil.randomBoolean();

        // when
        EzySimpleHandShakeParams sut = new EzySimpleHandShakeParams();
        sut.deserialize(
            EzyEntityArrays.newArray(
                clientId,
                clientKey,
                clientType,
                clientVersion,
                enableEncryption,
                reconnectToken
            )
        );

        // then
        Asserts.assertEquals(sut.getClientId(), clientId);
        Asserts.assertEquals(sut.getClientKey(), clientKey);
        Asserts.assertEquals(sut.getClientType(), clientType);
        Asserts.assertEquals(sut.getClientVersion(), clientVersion);
        Asserts.assertEquals(sut.getReconnectToken(), reconnectToken);
        Asserts.assertEquals(sut.isEnableEncryption(), enableEncryption);
    }
}
