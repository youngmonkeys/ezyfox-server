package com.tvd12.ezyfoxserver.admintools.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyNetworkPoint implements Serializable{
	private static final long serialVersionUID = -2488071811380761574L;
	
	private long inputBytes;
	private long outputBytes;
}
