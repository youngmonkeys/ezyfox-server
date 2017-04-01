package com.tvd12.ezyfoxserver.entity;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class EzySimpleUser extends EzyEntity implements EzyUser, Serializable {
	private static final long serialVersionUID = -7846882289922504595L;
	
	protected long id;
	protected String name;
	protected String password;
	protected transient EzySession session;
	
	private transient static final AtomicLong COUNTER;
	
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
