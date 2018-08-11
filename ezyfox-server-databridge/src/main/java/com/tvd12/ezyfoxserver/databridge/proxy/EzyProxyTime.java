package com.tvd12.ezyfoxserver.databridge.proxy;

import java.util.Date;

import com.tvd12.ezyfox.io.EzyDates;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EzyProxyTime {

	@Getter
	private final long millis;
	
	public String getTimestamp() {
		return EzyDates.format(new Date(millis), "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}
}
