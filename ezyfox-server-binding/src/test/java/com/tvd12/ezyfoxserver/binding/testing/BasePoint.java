package com.tvd12.ezyfoxserver.binding.testing;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasePoint {
	protected int x;
	protected int y;
	
	public String getXY() {
		return x + "-" + y;
	}
	
}
