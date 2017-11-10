package com.tvd12.ezyfoxserver.bean.testing.configuration;

import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

public class ConfigClassA {

	@EzySingleton
	public Singleton1 singleton1 = new Singleton1();
	
	@EzySingleton
	public Singleton1 singleton1duplicate = new Singleton1();
	
	@EzySingleton
	public Singleton2 getSingleton2() {
		return new Singleton2();
	}
	
	@EzySingleton
	public Singleton3 getSingleton3(Singleton1 s1, SingletonA1 a1, Singleton3A2 a2) {
		return new Singleton3(s1);
	}
	
	@EzySingleton
	public Singleton3A2 newSignleton3A2() {
		return new Singleton3A2();
	}
	
	@EzySingleton
	public void newInvalidSingleton() {
	}
	
	public Singleton1 newDuplicateSingleton1() {
		return new Singleton1();
	}
	
	@EzySingleton
	public AvailableSingleton1 newAvailableSingleton1() {
		return new AvailableSingleton1();
	}
	
}
