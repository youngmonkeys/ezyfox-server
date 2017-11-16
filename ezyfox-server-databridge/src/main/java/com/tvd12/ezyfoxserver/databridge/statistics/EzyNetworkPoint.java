package com.tvd12.ezyfoxserver.databridge.statistics;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyNetworkPoint implements Serializable {
	private static final long serialVersionUID = -6537078366231599664L;
	
	private long inputBytes;
	private long outputBytes;
	
}
