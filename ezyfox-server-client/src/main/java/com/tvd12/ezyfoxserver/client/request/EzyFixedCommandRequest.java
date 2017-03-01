package com.tvd12.ezyfoxserver.client.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EzyFixedCommandRequest implements EzyRequest {

	private Object data;
	
}
