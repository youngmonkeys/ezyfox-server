package com.tvd12.ezyfoxserver.codec;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EzySimpleMessageHeader implements EzyMessageHeader {

	protected boolean bigSize;
	protected boolean encrypted;
	protected boolean compressed;
	
}
