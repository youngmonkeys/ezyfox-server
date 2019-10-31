package com.tvd12.ezyfoxserver.testing.ssl;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactory;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactoryBuilder;
import com.tvd12.ezyfoxserver.ssl.EzySslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslContextFactory;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleSslContextFactoryTest extends BaseCoreTest {

    @Test
    public void test() throws Exception {
        new EzySimpleSslContextFactory() {
            @Override
            public String getAlgorithm(EzySslConfig config) {
                return super.getAlgorithm(config);
            }
        }.getAlgorithm(new EzySimpleSslConfig());
        
        EzySslContextFactory factory = new EzySimpleSslContextFactoryBuilder()
                .build();
        EzySimpleSslConfig sslConfig = new EzySimpleSslConfig();
        sslConfig.setKeyStoreFile("test-input/ssl-keystore.txt");
        sslConfig.setKeyStorePasswordFile("test-input/ssl-keystore-password.txt");
        sslConfig.setCertificatePasswordFile("test-input/ssl-certificate-password.txt");
        factory.newSslContext(sslConfig);
    }
    
}
