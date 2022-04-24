package com.tvd12.ezyfoxserver.testing.ext;

import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import org.testng.annotations.Test;

public class EzyAbstractPluginEntryLoaderTest {

    @Test
    public void newTest() {
        new EzyAbstractPluginEntryLoader() {
            @Override
            public EzyPluginEntry load() throws Exception {
                return null;
            }
        };
    }
}
