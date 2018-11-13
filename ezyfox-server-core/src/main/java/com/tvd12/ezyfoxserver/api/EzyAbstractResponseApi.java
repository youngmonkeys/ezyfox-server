package com.tvd12.ezyfoxserver.api;

import java.util.Collection;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;

public abstract class EzyAbstractResponseApi implements EzyResponseApi {
	
	@Override
	public void response(EzyPackage pack, boolean immediate) throws Exception {
		EzyConstant connectionType = getConnectionType();
		Collection<EzySession> recipients = pack.getRecipients(connectionType);
		if(recipients.isEmpty()) return;
		Object bytes = encodeData(pack.getData());
		if(immediate) {
        		for(EzySession session : recipients)
        		    session.sendNow(createPacket(bytes, pack));
		}
		else {
		    for(EzySession session : recipients)
                session.send(createPacket(bytes, pack));
		}
	}
	
    protected EzySimplePacket createPacket(Object bytes, EzyPackage pack) {
		EzySimplePacket packet = new EzySimplePacket();
		packet.setTransportType(pack.getTransportType());
		packet.setData(bytes);
		return packet;
    }
    
    protected abstract EzyConstant getConnectionType();
	
	protected abstract Object encodeData(EzyArray data) throws Exception;
	
}
