package com.tvd12.ezyfoxserver.bean.testing.configuration2;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.bean.testing.configuration.Singleton1;

import lombok.Setter;

public class ConfigClass1 {

	@Setter
	@EzyAutoBind
	private Singleton0 singleton0;
	
	@Setter
	@EzyAutoBind
	private Singleton1 singleton1;
	
	@EzyPrototype
	public Prototype3 prototype3 = new Prototype3();
	
	@EzyPrototype
	public Prototype0 newPrototype0() {
		return new Prototype0();
	}
	
	@EzyPrototype("p1")
	public Prototype1 newPrototype1(Prototype0 prototype0) {
		return new Prototype1(prototype0);
	}
	
	@EzyPostInit
	public void sayReady() {
		System.out.println("I'm ready, I have " + singleton0);
	}
	
}
