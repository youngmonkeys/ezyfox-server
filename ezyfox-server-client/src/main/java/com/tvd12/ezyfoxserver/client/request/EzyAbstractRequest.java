package com.tvd12.ezyfoxserver.client.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractRequest extends EzyFixedCommandRequest {

	private String command;
	
}
