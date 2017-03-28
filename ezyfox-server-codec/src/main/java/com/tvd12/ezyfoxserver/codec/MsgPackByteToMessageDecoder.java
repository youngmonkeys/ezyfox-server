package com.tvd12.ezyfoxserver.codec;

import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.PREPARE_MESSAGE;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_CONTENT;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_HEADER;
import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.READ_MESSAGE_SIZE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Setter;

public class MsgPackByteToMessageDecoder extends ByteToMessageDecoder {

	protected final Logger logger;
	protected final Handlers handlers;
	
	public MsgPackByteToMessageDecoder(EzyMessageDeserializer deserializer) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.handlers = Handlers.builder().deserializer(deserializer).build();
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, 
			ByteBuf in, List<Object> out) throws Exception {
		logger.debug("decode {} bytes", in);
		handlers.handle(ctx, in, out);
	}
	
}

@Setter
abstract class AbstractHandler implements EzyDecodeHandler {

	protected EzyDecodeHandler nextHandler;
	protected EzyMessageReader messageReader;
	
	@Override
	public EzyDecodeHandler nextHandler() {
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

class ReadMessageHeader extends AbstractHandler {

	@Override
	public EzyIDecodeState nextState() {
		return EzyDecodeState.READ_MESSAGE_HEADER;
	}

	@Override
	public boolean handle(ByteBuf in, List<Object> out) {
		return messageReader.readHeader(in);
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

@AllArgsConstructor
class ReadMessageContent extends AbstractHandler {

	protected EzyMessageDeserializer deserializer;
	
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
		return deserializer.deserialize(content);
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
		protected EzyMessageReader messageReader;
		protected EzyMessageDeserializer deserializer;
		
		{
			messageReader = new EzyMessageReader();
		}
		
		public Builder deserializer(EzyMessageDeserializer deserializer) {
			this.deserializer = deserializer;
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
			EzyDecodeHandler readMessageSize = new ReadMessageSize();
			EzyDecodeHandler readMessageContent = new ReadMessageContent(deserializer);
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
