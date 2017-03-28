package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;

import io.netty.channel.EventLoopGroup;

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
	
	/**
	 * set child group
	 * 
	 * @param childGroup the child group
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder childGroup(EventLoopGroup childGroup);
	
	/**
	 * set parent group
	 * 
	 * @param parentGroup the parent group
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder parentGroup(EventLoopGroup parentGroup);
	
	/**
	 * Set codec creator
	 * 
	 * @param codecCreator the creator
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder codecCreator(EzyCodecCreator codecCreator);
	
	/**
	 * Set ws codec creator
	 * 
	 * @param wsCodecCreator the ws creator
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder wsCodecCreator(EzyCodecCreator wsCodecCreator);

}
