package com.tvd12.ezyfoxserver.nio.testing.udp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import java.util.Set;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioUdpDataHandler;
import com.tvd12.ezyfoxserver.nio.udp.EzyNioUdpReader;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.reflect.MethodUtil;

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
	
	@Test
	public void processReadyKeysKeyInvalid() throws IOException {
		// given
		Selector ownSelector = mock(Selector.class);
		EzyNioUdpReader sut = new EzyNioUdpReader(1024);
		sut.setOwnSelector(ownSelector);
		
		SelectionKey selectionKey = mock(SelectionKey.class);
		when(selectionKey.isValid()).thenReturn(false);
		Set<SelectionKey> selectionKeys = Sets.newHashSet(selectionKey);
		when(ownSelector.selectedKeys()).thenReturn(selectionKeys);
		
		// when
		MethodUtil.invokeMethod("processReadyKeys", sut);
		
		// then
		verify(selectionKey, times(1)).isValid();
	}
	
	@Test
	public void processReadyKeyNotReadable() throws IOException {
		// given
		EzyNioUdpReader sut = new EzyNioUdpReader(1024);
		
		SelectionKey selectionKey = mock(SelectionKey.class);
		when(selectionKey.readyOps()).thenReturn(SelectionKey.OP_WRITE);

		// when
		MethodInvoker.create()
			.object(sut)
			.method("processReadyKey")
			.param(SelectionKey.class, selectionKey)
			.call();
		
		// then
		verify(selectionKey, times(1)).readyOps();
	}
	
	@Test
	public void processReadBytesByteCountLessIsZero() throws IOException {
		// given
		EzyNioUdpReader sut = new EzyNioUdpReader(1024);
		
		DatagramChannel channel = mock(DatagramChannel.class);
		InetSocketAddress address = new InetSocketAddress(3005);
		when(channel.receive(any())).thenReturn(address);

		// when
		MethodInvoker.create()
			.object(sut)
			.method("processReadBytes")
			.param(DatagramChannel.class, channel)
			.call();
		
		// then
		verify(channel, times(1)).receive(any());
	}
	
	public abstract static class MyDatagramChannel extends DatagramChannel {

		protected MyDatagramChannel() {
			super(SelectorProvider.provider());
		}
		
	}
}
