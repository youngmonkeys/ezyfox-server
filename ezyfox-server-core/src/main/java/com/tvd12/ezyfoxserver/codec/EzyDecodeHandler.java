package com.tvd12.ezyfoxserver.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface EzyDecodeHandler {

	/**
	 * Get next state
	 * 
	 * @return the next state
	 */
	EzyIDecodeState nextState();
	
	/**
	 * Get next handler corresponding the next state
	 * 
	 * @return the next handler
	 */
	EzyDecodeHandler nextHandler();
	
	/**
	 * Handler decoding
	 * 
	 * @param in the input
	 * @param out the output
	 * @return true if decoding is successful or not
	 */
	boolean handle(ByteBuf in, List<Object> out);
	
	/**
	 * Handler decoding
	 * 
	 * @param ctx the context
	 * @param in the input
	 * @param out the output
	 * @return true if decoding is successful or not
	 */
	default boolean handle(final ChannelHandlerContext ctx, 
			final ByteBuf in, final List<Object> out) {
		return handle(in, out);
	}
	
}
