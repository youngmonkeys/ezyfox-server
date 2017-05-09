package com.tvd12.ezyfoxserver.bean.testing.combine.pack1;

import com.tvd12.ezyfoxserver.annotation.EzyProperty;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.bean.testing.combine.EzyCombine0Ann;

import lombok.Setter;

@Setter
@EzyCombine0Ann
@EzyPrototype("a1")
public class ClassA1 {

	@EzyProperty("hello")
	private String hello;
	
	@EzyProperty
	public String foo;
	
	@EzyPostInit
	public void post() {
		System.out.println("\n\nI have hello = " + hello + ", and foo = " + foo + "\n\n");
	}
	
}
