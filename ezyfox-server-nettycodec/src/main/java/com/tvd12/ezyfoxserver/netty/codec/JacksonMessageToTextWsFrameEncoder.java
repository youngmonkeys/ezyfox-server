package com.tvd12.ezyfoxserver.netty.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class JacksonMessageToTextWsFrameEncoder extends MessageToMessageEncoder<EzyArray> {

	protected final Logger logger;
	protected final EzyMessageSerializer serializer;
	
	public JacksonMessageToTextWsFrameEncoder(EzyMessageSerializer serializer) {
		this.serializer = serializer;
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, EzyArray msg, List<Object> out) throws Exception {
		writeMessage(serializer.serialize(msg), out);
	}
	
	private void writeMessage(byte[] message, List<Object> out) {
		logger.debug("write message with size {}", message.length);
		out.add(new TextWebSocketFrame(Unpooled.wrappedBuffer(message)));
	}

}
