package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyBootstrap;
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
	 * Set codec creator
	 * 
	 * @param codecCreator the creator
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder codecCreator(EzyCodecCreator codecCreator);

	/**
	 * Set local bootstrap
	 * 
	 * @param localBootstrap the local bootstrap
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder localBootstrap(EzyBootstrap localBootstrap);
	
}
