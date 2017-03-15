package com.tvd12.ezyfoxserver.entity.impl;

import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class EzySimpleUser extends EzyEntity implements EzyUser {

	protected long id;
	protected String name;
	protected String password;
	protected EzySession session;
	
	private static final AtomicLong COUNTER;
	
	static {
		COUNTER = new AtomicLong(0);
	}
	
	public EzySimpleUser() {
		this.id = COUNTER.incrementAndGet();
	}
	
	@Override
	public void send(EzyData data) {
		session.send(data);
	}
	
}
