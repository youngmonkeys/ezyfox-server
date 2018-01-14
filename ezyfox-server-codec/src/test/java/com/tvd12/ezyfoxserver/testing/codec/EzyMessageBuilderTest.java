package com.tvd12.ezyfoxserver.testing.codec;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeader;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeaderBuilder;
import com.tvd12.test.base.BaseTest;

public class EzyMessageBuilderTest extends BaseTest {

	@Test
	public void test() {
		EzyMessageHeaderBuilder headerBuilder = EzyMessageHeaderBuilder.newInstance()
				.bigSize(true)
				.compressed(true)
				.encrypted(true);
		EzyMessage message = EzyMessageBuilder.newInstance()
				.size(5)
				.content(new byte[] {1, 2, 3, 4, 5})
				.header(headerBuilder)
				.build();
		EzyMessageHeader header = message.getHeader();
		assert header.isBigSize();
		assert header.isCompressed();
		assert header.isEncrypted();
		assert message.hasBigSize();
		assert message.getSize() == 5;
		assert message.getSizeLength() == 4;
		assert message.getContent().length == 5;
	}
	
	@Test
	public void test2() {
		EzyMessageHeaderBuilder headerBuilder = EzyMessageHeaderBuilder.newInstance()
				.bigSize(false)
				.compressed(true)
				.encrypted(true);
		EzyMessage message = EzyMessageBuilder.newInstance()
				.size(5)
				.content(new byte[] {1, 2, 3, 4, 5})
				.header(headerBuilder)
				.build();
		EzyMessageHeader header = message.getHeader();
		assert !header.isBigSize();
		assert header.isCompressed();
		assert header.isEncrypted();
		assert !message.hasBigSize();
		assert message.getSize() == 5;
		assert message.getSizeLength() == 2;
		assert message.getContent().length == 5;
		
		System.out.println(message.toString());
		System.out.println(header.toString());
	}
	
}
