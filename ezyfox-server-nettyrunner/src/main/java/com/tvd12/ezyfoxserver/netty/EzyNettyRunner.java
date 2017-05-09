package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.ezyfoxserver.EzyStarter.Builder;
import com.tvd12.ezyfoxserver.netty.EzyNettyStarter;

public class EzyNettyRunner extends EzyRunner {

	public static void main(String[] args) throws Exception {
		new EzyNettyRunner().run(args);
	}
	
	@Override
	protected Builder<?> newStarterBuilder() {
		return EzyNettyStarter.builder();
	}

}
