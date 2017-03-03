package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractRequest extends EzyFixedCommandRequest {

	private int appId;
	private EzyConstant command;
	
}
