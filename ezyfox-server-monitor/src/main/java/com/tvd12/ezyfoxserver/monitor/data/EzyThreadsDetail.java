package com.tvd12.ezyfoxserver.monitor.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyThreadsDetail {

	protected int threadsSize;
	protected long totalThreadsCpuTime;
	protected List<EzyThreadDetail> threads;
	
}
