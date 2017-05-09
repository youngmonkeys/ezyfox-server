package com.tvd12.ezyfoxserver.codec.testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.codec.MsgPackArrayTemplate;
import com.tvd12.test.base.BaseTest;

public class MsgPackArrayTemplateTest extends BaseTest {

	private MessagePack messagePack = new MessagePack();
	private MsgPackArrayTemplate template = new MsgPackArrayTemplate();
	
	@Test
	public void test() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Packer packer = messagePack.createPacker(stream);
		template.write(packer, null, false);
	}
	
	@Test(expectedExceptions = {NullPointerException.class})
	public void test1() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Packer packer = messagePack.createPacker(stream);
		template.write(packer, null, true);
	}
	
	@Test(expectedExceptions = {UnsupportedOperationException.class})
	public void test2() throws IOException {
		Unpacker unpacker = messagePack.createBufferUnpacker();
		template.read(unpacker, null);
	}
	
}
