package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;
import java.util.Map;

import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class MsgPackCodecCreator implements EzyCodecCreator {

	private MessagePack msgPack;
	
	public MsgPackCodecCreator() {
		this.msgPack = new MessagePack();
		this.msgPack.register(EzyObject.class, new EzyObjectTemplate());
	}
	
	@Override
	public ChannelOutboundHandler newEncoder() {
		return new MsgPackMessageToByteEncoder(msgPack);
	}

	@Override
	public ChannelInboundHandlerAdapter newDecoder() {
		return new MsgPackByteToMessageDecoder(msgPack);
	}

}

class EzyObjectTemplate extends AbstractTemplate<EzyObject> {
	
	@Override
	public void write(Packer pk, EzyObject target, boolean required) 
			throws IOException {
		if(target != null)
			writeNotNull(pk, target);
		else 
			writeNull(pk, required);
		
	}
	
	private void writeNull(Packer pk, boolean required) throws IOException {
        if (!required)
        	pk.writeNil();
        else
            throw new MessageTypeException("Attempted to write null");
	}
	
	private void writeNotNull(Packer pk, EzyObject target) throws IOException {
		pk.write(target.toMap());
	}

	@Override
	public EzyObject read(Unpacker u, EzyObject to, boolean required) throws IOException {
		 if (!required && u.trySkipNil()) 
	            return null;
		 return EzyFactory.create(EzyObjectBuilder.class)
				 .append(u.read(Map.class))
				 .build();
	}
}
