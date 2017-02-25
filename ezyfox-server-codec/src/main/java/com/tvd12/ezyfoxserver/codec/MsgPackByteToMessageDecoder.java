package com.tvd12.ezyfoxserver.codec;

import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.PREPARE_MESSAGE;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_CONTENT;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_SIZE;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.Setter;

public class MsgPackByteToMessageDecoder extends ByteToMessageDecoder {

	private Handlers handlers;
	
	public MsgPackByteToMessageDecoder(MessagePack msgPack) {
		this.handlers = Handlers.builder().msgPack(msgPack).build();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) 
			throws Exception {
		handlers.handle(in, out);
	}
	
}

interface Handler {
	
	EzyIDecodeState nextState();
	
	Handler nextHandler();
	
	boolean handle(ByteBuf in, List<Object> out);
	
}

@Setter
abstract class AbstractHandler implements Handler {
	
	protected Handler nextHandler;
	protected MessagePack msgPack;
	protected EzyMessageReader messageReader;
	
	@Override
	public Handler nextHandler() {
		return nextHandler;
	}
}

class PrepareMessage extends AbstractHandler {
	
	@Override
	public EzyIDecodeState nextState() {
		return READ_MESSAGE_SIZE;
	}
	
	@Override
	public boolean handle(ByteBuf in, List<Object> out) {
		messageReader.clear();
		return true;
	}
}

class ReadMessageSize extends AbstractHandler {

	@Override
	public EzyIDecodeState nextState() {
		return READ_MESSAGE_CONTENT;
	}

	@Override
	public boolean handle(ByteBuf in, List<Object> out) {
		return messageReader.readSize(in);
	}
}

class ReadMessageContent extends AbstractHandler {

	@Override
	public EzyIDecodeState nextState() {
		return PREPARE_MESSAGE;
	}

	@Override
	public boolean handle(ByteBuf in, List<Object> out) {
		if(!messageReader.readContent(in))
			return false;
		processMessage(messageReader.get(), out);
		return true;
	}
	
	private void processMessage(EzyMessage msg, List<Object> out) {
		out.add(readMessageContent(msg.getContent()));
	}
	
	private Object readMessageContent(byte[] content) {
		try {
			return msgPack.read(content);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}

class Handlers {
	
	private EzyIDecodeState state;
	private Map<EzyIDecodeState, Handler> handers;
	
	protected Handlers(Builder builder) {
		this.state = PREPARE_MESSAGE;
		this.handers = builder.newHandlers();
	}
	
	public void handle(ByteBuf in, List<Object> out) {
		Handler handler = handers.get(state);
		while(handler != null && handler.handle(in, out)) {
			state = handler.nextState();
			handler = handler.nextHandler();
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		protected MessagePack msgPack;
		protected EzyMessageReader messageReader;
		
		protected Builder() {
			this.messageReader = new EzyMessageReader();
		}
		
		public Builder msgPack(MessagePack msgPack) {
			this.msgPack = msgPack;
			return this;
		}
		
		public Handlers build() {
			return new Handlers(this);
		}
		
		public Map<EzyIDecodeState, Handler> newHandlers() {
			Handler prepareMessage = new PrepareMessage();
			Handler readMessageSize = new ReadMessageSize();
			Handler readMessageContent = new ReadMessageContent();
			Map<EzyIDecodeState, Handler> answer = new HashMap<>();
			answer.put(PREPARE_MESSAGE, newHandler(prepareMessage, readMessageSize));
			answer.put(READ_MESSAGE_SIZE, newHandler(readMessageSize, readMessageContent));
			answer.put(READ_MESSAGE_CONTENT, newHandler(readMessageContent));
			return answer;
		}
		
		
		private Handler newHandler(Handler handler) {
			return newHandler(handler, null);
		}
		
		private Handler newHandler(Handler handler, Handler next) {
			return newHandler((AbstractHandler)handler, next);
		}
		
		private Handler newHandler(AbstractHandler handler, Handler next) {
			handler.setMsgPack(msgPack);
			handler.setNextHandler(next);
			handler.setMessageReader(messageReader);
			return handler;
		}
	}
	
}
