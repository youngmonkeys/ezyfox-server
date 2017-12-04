package com.tvd12.ezyfoxserver.admintools.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EzyMemoryPoint implements Serializable{
	private static final long serialVersionUID = -212787182664660405L;
	
	private long maxMemory;
	private long freeMemory;
	private long totalMemory;
	
	private long usedMemory;
	private long allocatedMemory;
}
