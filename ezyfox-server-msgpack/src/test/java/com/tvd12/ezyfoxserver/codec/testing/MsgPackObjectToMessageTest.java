package com.tvd12.ezyfoxserver.codec.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyObjectToMessage;
import com.tvd12.ezyfoxserver.codec.MsgPackConstant;
import com.tvd12.ezyfoxserver.codec.MsgPackObjectToMessage;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;

public class MsgPackObjectToMessageTest extends BaseTest {

	@Test
	public void test() {
		EzyObjectToMessage converter = newMsgPackObjectToMessage();
		EzyArray params = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append("clientId")
				.append("token")
				.append(1)
				.build();
		EzyMessage message = converter.convert(params);
		assert !message.getHeader().isBigSize();
		assert !message.getHeader().isCompressed();
		assert !message.getHeader().isEncrypted();
		assert !message.hasBigSize();
		assert message.getContent().length > 0;
		assert message.getSize() > 0;
		assert message.getSizeLength() == 2;
		
	}
	
	@Test
	public void test2() {
		EzyObjectToMessage converter = newMsgPackObjectToMessage();
		EzyArray params = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append("clientId")
				.append("token")
				.append(1)
				.append(new int[MsgPackConstant.MAX_BIN16_SIZE])
				.build();
		EzyMessage message = converter.convert(params);
		assert message.getHeader().isBigSize();
		assert message.getSizeLength() == 4;
		
	}
	
	private EzyObjectToMessage newMsgPackObjectToMessage() {
		return MsgPackObjectToMessage.builder().build();
	}
}
