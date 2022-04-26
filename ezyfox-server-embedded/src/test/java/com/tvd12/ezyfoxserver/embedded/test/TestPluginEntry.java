package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;

public class TestPluginEntry extends EzySimplePluginEntry {

    @Override
    protected void preConfig(EzyPluginContext ctx) {
        getLogger().info("\n=================== TEST PLUGIN START CONFIG ================\n");
    }

    @Override
    protected void postConfig(EzyPluginContext ctx) {
        getLogger().info("\n=================== TEST PLUGIN END CONFIG ================\n");
    }

    @Override
    protected String[] getScanableBeanPackages() {
        return new String[]{"com.tvd12.ezyfoxserver.embedded.test.plugin"};
    }

    @Override
    protected void setupBeanContext(EzyPluginContext context, EzyBeanContextBuilder builder) {}
}
