package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;

import io.netty.channel.EventLoopGroup;

public interface EzyServerBootstrapBuilder extends EzyBuilder<EzyServerBootstrap> {

	/**
	 * set port
	 * 
	 * @param port the port
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder port(final int port);
	
	/**
	 * set child group
	 * 
	 * @param childGroup the child group
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder childGroup(final EventLoopGroup childGroup);
	
	/**
	 * set parent group
	 * 
	 * @param parentGroup the parent group
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder parentGroup(final EventLoopGroup parentGroup);
	
	/**
	 * Set combined codec handler class name
	 * 
	 * @param className the class name
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder combinedCodecHandler(final String className);
	
}
