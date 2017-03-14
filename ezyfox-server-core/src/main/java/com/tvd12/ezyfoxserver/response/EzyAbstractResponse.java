package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractResponse extends EzyFixedCommandResponse {

	private int appId;
	private EzyConstant command;
	
}
