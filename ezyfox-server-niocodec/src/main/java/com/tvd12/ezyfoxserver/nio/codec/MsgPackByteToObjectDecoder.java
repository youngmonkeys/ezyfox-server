package com.tvd12.ezyfoxserver.nio.codec;

import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.PREPARE_MESSAGE;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_CONTENT;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_HEADER;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_SIZE;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Queue;

import com.tvd12.ezyfoxserver.codec.EzyByteBufferMessageReader;
import com.tvd12.ezyfoxserver.codec.EzyDecodeState;
import com.tvd12.ezyfoxserver.codec.EzyIDecodeState;
import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioByteToObjectDecoder;

import lombok.Setter;

public class MsgPackByteToObjectDecoder implements EzyNioByteToObjectDecoder {

	protected final Handlers handlers;
	protected final EzyMessageDeserializer deserializer;
	
	public MsgPackByteToObjectDecoder(
			EzyMessageDeserializer deserializer, int maxSize) {
		this.deserializer = deserializer;
		this.handlers = Handlers.builder()
				.maxSize(maxSize)
				.build();
	}
	
	@Override
	public Object decode(EzyMessage message) throws Exception {
		return deserializer.deserialize(message.getContent());
	}
	
	@Override
	public void decode(ByteBuffer bytes, Queue<EzyMessage> out) throws Exception {
		handlers.handle(bytes, out);
	}
	
}

@Setter
abstract class AbstractHandler implements EzyDecodeHandler {

	protected EzyDecodeHandler nextHandler;
	protected EzyByteBufferMessageReader messageReader;
	
	@Override
	public EzyDecodeHandler nextHandler() {
		return nextHandler;
	}
}

class PrepareMessage extends AbstractHandler {
	
	@Override
	public EzyIDecodeState nextState() {
		return READ_MESSAGE_HEADER;
	}
	
	@Override
	public boolean handle(ByteBuffer in, Queue<EzyMessage> out) {
		messageReader.clear();
		return true;
	}
}

class ReadMessageHeader extends AbstractHandler {

	@Override
	public EzyIDecodeState nextState() {
		return EzyDecodeState.READ_MESSAGE_SIZE;
	}

	@Override
	public boolean handle(ByteBuffer in, Queue<EzyMessage> out) {
		return messageReader.readHeader(in);
	}
	
}

class ReadMessageSize extends AbstractHandler {

	protected final int maxSize;
	
	public ReadMessageSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public EzyIDecodeState nextState() {
		return READ_MESSAGE_CONTENT;
	}

	@Override
	public boolean handle(ByteBuffer in, Queue<EzyMessage> out) {
		return messageReader.readSize(in, maxSize);
	}
}

class ReadMessageContent extends AbstractHandler {
	
	@Override
	public EzyIDecodeState nextState() {
		return PREPARE_MESSAGE;
	}

	@Override
	public boolean handle(ByteBuffer in, Queue<EzyMessage> out) {
		if(!messageReader.readContent(in))
			return false;
		out.add(messageReader.get());
		return true;
	}
	
}

class Handlers extends EzyDecodeHandlers {
	
	protected Handlers(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends AbstractBuilder {
		protected int maxSize;
		protected EzyByteBufferMessageReader messageReader = new EzyByteBufferMessageReader();
		
		public Builder maxSize(int maxSize) {
			this.maxSize = maxSize;
			return this;
		}
		
		public Handlers build() {
			return new Handlers(this);
		}
		
		@Override
		protected void addHandlers(
				Map<EzyIDecodeState, EzyDecodeHandler> answer) {
			EzyDecodeHandler readMessgeHeader = new ReadMessageHeader();
			EzyDecodeHandler prepareMessage = new PrepareMessage();
			EzyDecodeHandler readMessageSize = new ReadMessageSize(maxSize);
			EzyDecodeHandler readMessageContent = new ReadMessageContent();
			answer.put(PREPARE_MESSAGE, newHandler(prepareMessage, readMessgeHeader));
			answer.put(READ_MESSAGE_HEADER, newHandler(readMessgeHeader, readMessageSize));
			answer.put(READ_MESSAGE_SIZE, newHandler(readMessageSize, readMessageContent));
			answer.put(READ_MESSAGE_CONTENT, newHandler(readMessageContent));
		}
		
		
		private EzyDecodeHandler newHandler(EzyDecodeHandler handler) {
			return newHandler(handler, null);
		}
		
		private EzyDecodeHandler newHandler(EzyDecodeHandler handler, EzyDecodeHandler next) {
			return newHandler((AbstractHandler)handler, next);
		}
		
		private EzyDecodeHandler newHandler(AbstractHandler handler, EzyDecodeHandler next) {
			handler.setNextHandler(next);
			handler.setMessageReader(messageReader);
			return handler;
		}
	}
	
}
