package com.tvd12.ezyfoxserver.testing.ssl;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactory;
import com.tvd12.ezyfoxserver.ssl.EzySslConfig;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleSslContextFactoryTest extends BaseCoreTest {

    @Test
    public void test() {
        new EzySimpleSslContextFactory() {
            @Override
            public String getAlgorithm(EzySslConfig config) {
                return super.getAlgorithm(config);
            }
        }.getAlgorithm(new EzySimpleSslConfig());
    }
    
}
