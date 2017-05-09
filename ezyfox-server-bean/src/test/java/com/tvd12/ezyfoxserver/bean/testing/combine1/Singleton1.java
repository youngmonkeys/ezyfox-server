package com.tvd12.ezyfoxserver.bean.testing.combine1;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Singleton1 {

	@EzyAutoBind
	private Singleton0 singleton0;
	
	@EzyAutoBind
	private Singleton2 singleton2;
	
}
