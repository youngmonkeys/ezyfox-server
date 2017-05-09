package com.tvd12.ezyfoxserver.bean.testing.singleton;

import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Getter;

@Getter
@EzySingleton("b")
public class ClassB {

	private String value2 = "2";
	
}
