package com.tvd12.ezyfoxserver.databridge.statistics;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyMemoryPoint implements Serializable {
	private static final long serialVersionUID = -8664380030251940944L;
	
	protected long maxMemory;
	protected long freeMemory;
	protected long totalMemory;
	
	public long getAllocatedMemory() {
		return totalMemory;
	}
	
	public long getUsedMemory() {
		return totalMemory - freeMemory;
	}
	
}
