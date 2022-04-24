package com.tvd12.ezyfoxserver.testing.ssl;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactory;
import com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactoryBuilder;
import com.tvd12.ezyfoxserver.ssl.EzySslConfig;
import com.tvd12.ezyfoxserver.ssl.EzySslContextFactory;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

import static org.mockito.Mockito.*;

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
    
    @Test
    public void getAlgorithmTest() {
        // given
        EzySimpleSslContextFactory sut = new EzySimpleSslContextFactory() {
            protected String getDefaultAlgorithm() {
                return null;
            };
        };
        EzySslConfig sslConfig = mock(EzySslConfig.class);
        
        // when
        String algorithm = MethodInvoker.create()
                .object(sut)
                .method("getAlgorithm")
                .param(EzySslConfig.class, sslConfig)
                .call();
        
        // then
        Asserts.assertEquals("SunX509", algorithm);
    }
}
