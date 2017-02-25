package com.tvd12.ezyfoxserver.codec;

import static com.tvd12.ezyfoxserver.codec.EzyDecodeState.*;

import java.io.IOException;
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
	protected void decode(ChannelHandlerContext ctx, 
			ByteBuf in, List<Object> out) throws Exception {
		handlers.handle(ctx, in, out);
	}
	
}

@Setter
abstract class AbstractHandler implements EzyDecodeHandler {
	
	protected EzyDecodeHandler nextHandler;
	protected MessagePack msgPack;
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

class Handlers extends EzyDecodeHandlers {
	
	protected Handlers(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends AbstractBuilder {
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
		
		@Override
		protected void addHandlers(
				Map<EzyIDecodeState, EzyDecodeHandler> answer) {
			EzyDecodeHandler readMessgeHeader = new ReadMessageHeader();
			EzyDecodeHandler prepareMessage = new PrepareMessage();
			EzyDecodeHandler readMessageSize = new ReadMessageSize();
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
			handler.setMsgPack(msgPack);
			handler.setNextHandler(next);
			handler.setMessageReader(messageReader);
			return handler;
		}
	}
	
}
