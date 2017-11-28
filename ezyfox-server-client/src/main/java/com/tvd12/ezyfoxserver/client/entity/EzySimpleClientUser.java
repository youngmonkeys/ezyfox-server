package com.tvd12.ezyfoxserver.client.entity;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzySimpleClientUser extends EzyEntity implements EzyClientUser, Serializable {
	private static final long serialVersionUID = -7230916678168758064L;

	protected long id;
	protected String name;
	protected EzyClientSession session;
	
	@Override
	public void send(EzyData data, EzyTransportType type) {
		session.send(data);
	}
	
	@Override
	public void sendNow(EzyData data, EzyTransportType type) {
		send(data, type);
	}
	
	@Override
	public void destroy() {
		session.destroy();
	}
	
}
