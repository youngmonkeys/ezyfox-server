package com.tvd12.ezyfoxserver.bean.testing.configuration1;

import lombok.Getter;

@Getter
public class Singleton3 {

	private final Singleton1 s1;
	
	public Singleton3(Singleton1 s1) {
		this.s1 = s1;
	}
	
}
