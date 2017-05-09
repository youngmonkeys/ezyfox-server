package com.tvd12.ezyfoxserver.bean.testing.configuration2;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Prototype2 {

	@Getter
	private final Prototype0 prototype0;
	
	@EzyAutoBind
	private Singleton0 singleton0;
	
	public Prototype2(Prototype0 prototype0) {
		this.prototype0 = prototype0;
	}
	
	@EzyPostInit
	public void sayThank() {
		System.out.println("thank you! I have " + prototype0 + " and " + singleton0);
	}
	
}
