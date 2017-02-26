package com.tvd12.ezyfoxserver.entity.impl;

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
	protected EzySession session;
	
}
