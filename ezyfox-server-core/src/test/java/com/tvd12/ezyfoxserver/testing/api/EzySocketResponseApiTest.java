package com.tvd12.ezyfoxserver.testing.api;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.api.EzySocketResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.util.RandomUtil;

public class EzySocketResponseApiTest {
	
	@Test
	public void normalResponseTest() throws Exception {
		// given
		EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
		EzySocketResponseApi sut = new EzySocketResponseApi(encoder);
		
		EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
		EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(false);
        pack.setTransportType(EzyTransportType.TCP);
        
        int sessionCount = RandomUtil.randomSmallInt();
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
        	EzySession session = mock(EzySession.class);
        	when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        	return session;
        });
        pack.addRecipients(sessions);
		
		
		// when
		sut.response(pack);
		
		// then
		verify(encoder, times(1)).encode(data);
		verify(encoder, times(0)).toMessageContent(data);
		verify(encoder, times(0)).encryptMessageContent(any(byte[].class), any(byte[].class));
	}
	

	@Test
	public void secureResponseTest() throws Exception {
		// given
		EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
		EzySocketResponseApi sut = new EzySocketResponseApi(encoder);
		
		EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
		EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(true);
        pack.setTransportType(EzyTransportType.TCP);
        
        int sessionCount = RandomUtil.randomSmallInt();
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
        	EzySession session = mock(EzySession.class);
        	when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        	return session;
        });
        pack.addRecipients(sessions);
		
		
		// when
		sut.response(pack);
		
		// then
		verify(encoder, times(1)).toMessageContent(data);
		verify(encoder, times(sessionCount)).encryptMessageContent(any(byte[].class), any(byte[].class));
	}
	
	@Test
	public void secureResponseImmediateTest() throws Exception {
		// given
		EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
		EzySocketResponseApi sut = new EzySocketResponseApi(encoder);
		
		EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
		EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(true);
        pack.setTransportType(EzyTransportType.TCP);
        
        int sessionCount = RandomUtil.randomSmallInt();
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
        	EzySession session = mock(EzySession.class);
        	when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        	return session;
        });
        pack.addRecipients(sessions);
		
		
		// when
		sut.response(pack, true);
		
		// then
		verify(encoder, times(1)).toMessageContent(data);
		verify(encoder, times(sessionCount)).encryptMessageContent(any(byte[].class), any(byte[].class));
	}
}
