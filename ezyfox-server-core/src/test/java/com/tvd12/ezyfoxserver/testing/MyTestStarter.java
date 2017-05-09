package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;

public class MyTestStarter extends EzyStarter {

	protected MyTestStarter(Builder builder) {
		super(builder);
	}

	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new MyTestServerBootstrapBuilder();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyStarter.Builder<Builder> {
		@Override
		public EzyStarter build() {
			return new MyTestStarter(this);
		}
	}

}
