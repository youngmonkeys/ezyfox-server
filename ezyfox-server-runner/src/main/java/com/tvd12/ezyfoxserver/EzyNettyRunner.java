package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.EzyStarter.Builder;

public class EzyNettyRunner extends EzyRunner {

	public static void main(String[] args) throws Exception {
		new EzyNettyRunner().run(args);
	}
	
	@Override
	protected Builder<?> newStarterBuilder() {
		return EzyNettyStarter.builder();
	}

}
