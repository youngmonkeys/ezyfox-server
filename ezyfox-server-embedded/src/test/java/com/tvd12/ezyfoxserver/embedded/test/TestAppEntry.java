package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;

public class TestAppEntry extends EzySimpleAppEntry {

	@Override
	protected void preConfig(EzyAppContext ctx) {
		getLogger().info("\n=================== FREE CHAT APP START CONFIG ================\n");
	}
	
	@Override
	protected void postConfig(EzyAppContext ctx) {
		getLogger().info("\n=================== FREE CHAT APP END CONFIG ================\n");
	}

	@Override
	protected String[] getScanableBeanPackages() {
		return new String[0];
	}

	@Override
	protected String[] getScanableBindingPackages() {
		return new String[0];
	}

	@Override
	protected void setupBeanContext(EzyAppContext context, EzyBeanContextBuilder builder) {
	}
	
}
