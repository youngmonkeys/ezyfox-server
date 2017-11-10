package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.tvd12.ezyfoxserver.codec.EzyDecodeState;
import com.tvd12.ezyfoxserver.codec.EzyIDecodeState;
import com.tvd12.ezyfoxserver.codec.EzyMessage;

public abstract class EzyDecodeHandlers {

	protected EzyIDecodeState state;
	protected Map<EzyIDecodeState, EzyDecodeHandler> handers;
	
	protected EzyDecodeHandlers(AbstractBuilder builder) {
		this.state = firstState();
		this.handers = builder.newHandlers();
	}
	
	protected void handle(ByteBuffer in, Queue<EzyMessage> out) {
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
