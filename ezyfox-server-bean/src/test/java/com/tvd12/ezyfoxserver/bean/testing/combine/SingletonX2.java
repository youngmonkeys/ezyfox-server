package com.tvd12.ezyfoxserver.bean.testing.combine;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SingletonX2 {

	@EzyAutoBind
	private SingletonX1 singletonX1;
	
	@EzyAutoBind
	private SingletonX2 singletonX2;
	
}
