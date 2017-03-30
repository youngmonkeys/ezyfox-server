package com.tvd12.ezyfoxserver.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class EzyDecodeHandlers {

	protected EzyIDecodeState state;
	protected Map<EzyIDecodeState, EzyDecodeHandler> handers;
	
	protected EzyDecodeHandlers(AbstractBuilder builder) {
		this.state = firstState();
		this.handers = builder.newHandlers();
	}
	
	public void handle(ChannelHandlerContext ctx, 
			ByteBuf in, List<Object> out) {
		handle(in, out);
	}
	
	protected void handle(ByteBuf in, List<Object> out) {
		EzyDecodeHandler handler = handers.get(state);
		while(handler != null && handler.handle(in, out)) {
			state = handler.nextState();
			handler = handler.nextHandler();
		}
	}
	
	protected EzyIDecodeState firstState() {
		return EzyDecodeState.PREPARE_MESSAGE;
	}
	
	public abstract static class AbstractBuilder {
		protected Map<EzyIDecodeState, EzyDecodeHandler> newHandlers() {
			Map<EzyIDecodeState, EzyDecodeHandler> answer = new HashMap<>();
			addHandlers(answer);
			return answer;
		}
		
		protected abstract void addHandlers(Map<EzyIDecodeState, EzyDecodeHandler> answer);
	}
	
}
