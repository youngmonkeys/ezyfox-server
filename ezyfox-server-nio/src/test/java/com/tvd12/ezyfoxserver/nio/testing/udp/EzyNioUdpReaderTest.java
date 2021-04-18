package com.tvd12.ezyfoxserver.nio.testing.udp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioUdpDataHandler;
import com.tvd12.ezyfoxserver.nio.udp.EzyNioUdpReader;

public class EzyNioUdpReaderTest {

	@Test
	public void test() throws Exception {
		EzyNioUdpReader reader = new EzyNioUdpReader(1);
		Selector selector = spy(Selector.class);
		when(selector.select()).thenReturn(1);
		reader.setOwnSelector(selector);
		EzyNioUdpDataHandler dataHandler = mock(EzyNioUdpDataHandler.class);
		reader.setUdpDataHandler(dataHandler);
		SelectionKey selectionKey = spy(SelectionKey.class);
		when(selector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey));
		when(selectionKey.isValid()).thenReturn(true);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_READ);
		MyDatagramChannel channel = mock(MyDatagramChannel.class);
		when(selectionKey.channel()).thenReturn(channel);
		when(channel.receive(any(ByteBuffer.class))).thenAnswer(new Answer<InetSocketAddress>() {
			@Override
			public InetSocketAddress answer(InvocationOnMock invocation) throws Throwable {
				ByteBuffer buffer = invocation.getArgumentAt(0, ByteBuffer.class);
				buffer.put((byte)0);
				return new InetSocketAddress(12346);
			}
		});
		reader.handleEvent();
	}
	
	@Test
	public void testAddressNull() throws Exception {
		EzyNioUdpReader reader = new EzyNioUdpReader(1);
		Selector selector = spy(Selector.class);
		when(selector.select()).thenReturn(1);
		reader.setOwnSelector(selector);
		EzyNioUdpDataHandler dataHandler = mock(EzyNioUdpDataHandler.class);
		reader.setUdpDataHandler(dataHandler);
		SelectionKey selectionKey = spy(SelectionKey.class);
		when(selector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey));
		when(selectionKey.isValid()).thenReturn(true);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_READ);
		MyDatagramChannel channel = mock(MyDatagramChannel.class);
		when(selectionKey.channel()).thenReturn(channel);
		reader.handleEvent();
	}
	
	@Test
	public void testClosedSelectorExceptionCase() throws Exception {
		EzyNioUdpReader reader = new EzyNioUdpReader(1);
		Selector selector = spy(Selector.class);
		when(selector.select()).thenReturn(1);
		reader.setOwnSelector(selector);
		EzyNioUdpDataHandler dataHandler = mock(EzyNioUdpDataHandler.class);
		reader.setUdpDataHandler(dataHandler);
		SelectionKey selectionKey = spy(SelectionKey.class);
		when(selector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey));
		when(selectionKey.isValid()).thenReturn(true);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_READ);
		MyDatagramChannel channel = mock(MyDatagramChannel.class);
		when(selectionKey.channel()).thenReturn(channel);
		when(channel.receive(any(ByteBuffer.class))).thenThrow(new ClosedSelectorException());
		reader.handleEvent();
	}
	
	@Test
	public void testCancelledKeyExceptionCase() throws Exception {
		EzyNioUdpReader reader = new EzyNioUdpReader(1);
		Selector selector = spy(Selector.class);
		when(selector.select()).thenReturn(1);
		reader.setOwnSelector(selector);
		EzyNioUdpDataHandler dataHandler = mock(EzyNioUdpDataHandler.class);
		reader.setUdpDataHandler(dataHandler);
		SelectionKey selectionKey = spy(SelectionKey.class);
		when(selector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey));
		when(selectionKey.isValid()).thenReturn(true);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_READ);
		MyDatagramChannel channel = mock(MyDatagramChannel.class);
		when(selectionKey.channel()).thenReturn(channel);
		when(channel.receive(any(ByteBuffer.class))).thenThrow(new CancelledKeyException());
		reader.handleEvent();
	}
	
	@Test
	public void testIOExceptionCase() throws Exception {
		EzyNioUdpReader reader = new EzyNioUdpReader(1);
		Selector selector = spy(Selector.class);
		when(selector.select()).thenReturn(1);
		reader.setOwnSelector(selector);
		EzyNioUdpDataHandler dataHandler = mock(EzyNioUdpDataHandler.class);
		reader.setUdpDataHandler(dataHandler);
		SelectionKey selectionKey = spy(SelectionKey.class);
		when(selector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey));
		when(selectionKey.isValid()).thenReturn(true);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_READ);
		MyDatagramChannel channel = mock(MyDatagramChannel.class);
		when(selectionKey.channel()).thenReturn(channel);
		when(channel.receive(any(ByteBuffer.class))).thenThrow(new IOException("test"));
		reader.handleEvent();
	}
	
	@Test
	public void testExceptionCase() throws Exception {
		EzyNioUdpReader reader = new EzyNioUdpReader(1);
		Selector selector = spy(Selector.class);
		when(selector.select()).thenReturn(1);
		reader.setOwnSelector(selector);
		EzyNioUdpDataHandler dataHandler = mock(EzyNioUdpDataHandler.class);
		reader.setUdpDataHandler(dataHandler);
		SelectionKey selectionKey = spy(SelectionKey.class);
		when(selector.selectedKeys()).thenReturn(Sets.newHashSet(selectionKey));
		when(selectionKey.isValid()).thenReturn(true);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_READ);
		MyDatagramChannel channel = mock(MyDatagramChannel.class);
		when(selectionKey.channel()).thenReturn(channel);
		when(channel.receive(any(ByteBuffer.class))).thenThrow(new IllegalStateException("test"));
		reader.handleEvent();
	}
	
	public abstract static class MyDatagramChannel extends DatagramChannel {

		protected MyDatagramChannel() {
			super(SelectorProvider.provider());
		}
		
	}
}
