package com.tvd12.ezyfoxserver.databridge.statistics;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyCpuPoint implements Serializable {
	private static final long serialVersionUID = 9034906186633586693L;

	private double systemCpuLoad;
	private double processCpuLoad;
	
}
