package com.tvd12.ezyfoxserver.netty.entity;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzySimpleSession extends EzyAbstractSession implements EzyNettySession {
	private static final long serialVersionUID = -6257434105426561446L;
	
}
