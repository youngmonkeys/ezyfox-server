package com.tvd12.ezyfoxserver.nio.testing;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.creator.EzySessionCreator;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyHandlerGroupBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.test.assertion.Asserts;

public class EzyHandlerGroupBuilderFactoryImplTest {

    @Test
    public void buildWithSessionCreatorNotNull() {
        // given
        EzySessionCreator sessionCreator = mock(EzySessionCreator.class);
        EzyHandlerGroupBuilderFactoryImpl.Builder builder = 
                EzyHandlerGroupBuilderFactoryImpl.builder()
                .sessionCreator(sessionCreator);
        
        // when
        EzyHandlerGroupBuilderFactory factor = builder.build();
        
        // then
        Asserts.assertNotNull(factor);
    }
}
