package com.tvd12.ezyfoxserver.bean.testing.singleton;

import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Getter;

@Getter
@EzySingleton("a")
public class ClassA {

	private String value1 = "1";
	
}
