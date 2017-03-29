package com.tvd12.ezyfoxserver.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EzyFixedCommandResponse 
		extends EzyBaseResponse 
		implements EzyResponse {

	private Object data;
	
}
