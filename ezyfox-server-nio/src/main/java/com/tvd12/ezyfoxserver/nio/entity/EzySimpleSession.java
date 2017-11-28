package com.tvd12.ezyfoxserver.nio.entity;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class EzySimpleSession extends EzyAbstractSession implements EzyNioSession {
	private static final long serialVersionUID = -8390274886953462147L;
}
