package com.tvd12.ezyfoxserver.testing.socket;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyEmptyObject;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.test.assertion.Asserts;

import static org.mockito.Mockito.*;

public class EzySessionTicketsRequestQueuesTest {

	@Test
	public void addSystemRequest() {
		// given
		EzySession session = mock(EzySession.class);
		EzyArray data = EzyEntityArrays.newArray(
				EzyCommand.APP_ACCESS.getId(), 
				EzyEmptyObject.getInstance());
		EzySocketRequest socketRequest = new EzySimpleSocketRequest(session, data);
		
		EzyRequestQueue queue = mock(EzyRequestQueue.class);
		when(queue.isEmpty()).thenReturn(false);
		when(queue.add(socketRequest)).thenReturn(false);
		when(session.getSystemRequestQueue()).thenReturn(queue);
		
		EzySessionTicketsRequestQueues sut = new EzySessionTicketsRequestQueues();
		
		// when
		boolean result = sut.addRequest(socketRequest);
		
		// then
		Asserts.assertFalse(result);
		Asserts.assertTrue(sut.getSystemQueue().isEmpty());
	}
	
	@Test
	public void addSystemRequestWithEmptyQueue() {
		// given
		EzySession session = mock(EzySession.class);
		EzyArray data = EzyEntityArrays.newArray(
				EzyCommand.APP_ACCESS.getId(), 
				EzyEmptyObject.getInstance());
		EzySocketRequest socketRequest = new EzySimpleSocketRequest(session, data);
		
		EzyRequestQueue queue = mock(EzyRequestQueue.class);
		when(queue.isEmpty()).thenReturn(true);
		when(session.getSystemRequestQueue()).thenReturn(queue);
		
		EzySessionTicketsRequestQueues sut = new EzySessionTicketsRequestQueues();
		
		// when
		boolean result = sut.addRequest(socketRequest);
		
		// then
		Asserts.assertFalse(result);
		Asserts.assertTrue(sut.getSystemQueue().isEmpty());
	}
}
