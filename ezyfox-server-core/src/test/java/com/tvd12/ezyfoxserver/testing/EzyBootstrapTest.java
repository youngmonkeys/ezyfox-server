package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import org.testng.annotations.Test;

public class EzyBootstrapTest extends BaseCoreTest {

    @Test
    public void destroyTest() {
        EzyBootstrap bootstrap = EzyBootstrap.builder()
                .context(newServerContext())
                .build();
        bootstrap.destroy();
    }
}
