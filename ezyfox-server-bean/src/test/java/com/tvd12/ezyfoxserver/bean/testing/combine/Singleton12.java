package com.tvd12.ezyfoxserver.bean.testing.combine;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.testing.combine.pack1.Singleton1;
import com.tvd12.ezyfoxserver.bean.testing.combine.pack2.Singleton2;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Singleton12 {

	@EzyAutoBind("s1")
	private Singleton1 singleton1;
	
	@EzyAutoBind("s2")
	private Singleton2 singleton2;
	
}
