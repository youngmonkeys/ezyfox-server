package com.tvd12.ezyfoxserver.bean.testing.configuration;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Singleton1 {

	@EzyAutoBind
	private Singleton0 singleton0;
	
	@EzyPostInit
	public void hi() {
		System.out.println("\nHi! I come from Singleton1, do you know me?\n");
	}
	
}
