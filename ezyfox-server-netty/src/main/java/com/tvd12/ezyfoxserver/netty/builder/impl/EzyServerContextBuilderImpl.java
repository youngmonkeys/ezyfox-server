package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.builder.EzySimpleServerContextBuilder;

public class EzyServerContextBuilderImpl 
		extends EzySimpleServerContextBuilder<EzyServerContextBuilderImpl> {

	public static EzyServerContextBuilderImpl builder() {
		return new EzyServerContextBuilderImpl();
	}
}
