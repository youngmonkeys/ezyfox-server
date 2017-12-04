package com.tvd12.ezyfoxserver.admintools.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyCpuPoint implements Serializable {
	private static final long serialVersionUID = 8079401711865975657L;

	private double systemCpuLoad;
	private double processCpuLoad;
}
