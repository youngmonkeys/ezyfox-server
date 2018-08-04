package com.tvd12.ezyfoxserver.netty.codec;

import static com.tvd12.ezyfox.codec.EzyDecodeState.PREPARE_MESSAGE;
import static com.tvd12.ezyfox.codec.EzyDecodeState.READ_MESSAGE_CONTENT;
import static com.tvd12.ezyfox.codec.EzyDecodeState.READ_MESSAGE_HEADER;
import static com.tvd12.ezyfox.codec.EzyDecodeState.READ_MESSAGE_SIZE;

import java.util.List;
import java.util.Map;

import com.tvd12.ezyfox.codec.EzyDecodeState;
import com.tvd12.ezyfox.codec.EzyIDecodeState;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.handler.EzyBytesReceived;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Setter;

public class MsgPackByteToMessageDecoder extends ByteToMessageDecoder {

	protected final Handlers handlers;
	
	public MsgPackByteToMessageDecoder(
			EzyMessageDeserializer deserializer, int maxSize) {
		this.handlers = Handlers.builder()
				.maxSize(maxSize)
				.deserializer(deserializer)
				.build();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf bytes = (ByteBuf)msg;
		int bytesCount = bytes.readableBytes(); 
		super.channelRead(ctx, msg);
		EzyBytesReceived delegate = (EzyBytesReceived)ctx.pipeline().get("handler");
		delegate.bytesReceived(bytesCount);
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
	protected EzyByteBufMessageReader messageReader;
	
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
	public boolean handle(ByteBuf in, List<Object> out) {
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
	public boolean handle(ByteBuf in, List<Object> out) {
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
	public boolean handle(ByteBuf in, List<Object> out) {
		return messageReader.readSize(in, maxSize);
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
		Object contentBytes = readMessageContent(msg.getContent());
		out.add(contentBytes);
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
		protected int maxSize;
		protected EzyMessageDeserializer deserializer;
		protected EzyByteBufMessageReader messageReader = new EzyByteBufMessageReader();
		
		public Builder maxSize(int maxSize) {
			this.maxSize = maxSize;
			return this;
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
			EzyDecodeHandler readMessageSize = new ReadMessageSize(maxSize);
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
