package com.tvd12.ezyfoxserver.codec;

import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;

public class MsgPackCodecTest extends CodecBaseTest {

	protected MessagePack msgPack;
	
	public MsgPackCodecTest() {
		this.msgPack = new MessagePack();
		this.msgPack.register(EzyObject.class, new MsgPackObjectTemplate());
		this.msgPack.register(EzyArray.class, new MsgPackArrayTemplate());
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
