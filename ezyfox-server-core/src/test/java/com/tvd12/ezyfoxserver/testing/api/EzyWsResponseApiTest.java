package com.tvd12.ezyfoxserver.testing.api;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.codec.EzyObjectToStringEncoder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.api.EzyWsResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.util.RandomUtil;

public class EzyWsResponseApiTest {

	@Test
	public void responseTest() throws Exception {
		// given
		EzyObjectToStringEncoder encoder = mock(EzyObjectToStringEncoder.class);
		EzyWsResponseApi sut = new EzyWsResponseApi(encoder);
		
		EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
		EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(true);
        pack.setTransportType(EzyTransportType.TCP);
        
        int sessionCount = RandomUtil.randomSmallInt() + 1;
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
        	EzySession session = mock(EzySession.class);
        	when(session.getConnectionType()).thenReturn(EzyConnectionType.WEBSOCKET);
        	return session;
        });
        pack.addRecipients(sessions);
		
		
		// when
		sut.response(pack);
		
		// then
		verify(encoder, times(1)).encode(data, String.class);
		verify(encoder, times(0)).toMessageContent(data);
		verify(encoder, times(0)).encryptMessageContent(any(byte[].class), any(byte[].class));
	}
	
}
