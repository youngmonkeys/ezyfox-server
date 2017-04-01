package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;

public interface EzyServerBootstrapBuilder extends EzyBuilder<EzyServerBootstrap> {

	/**
	 * set port
	 * 
	 * @param port the port
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder port(int port);
	
	/**
	 * set ws port
	 * 
	 * @param wsport the  ws port
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder wsport(int wsport);
	
	/**
	 * set server
	 * 
	 * @param boss the server
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder boss(EzyServer boss);
	
}
