package com.tvd12.ezyfoxserver.bean.testing.prototype;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;

import lombok.Getter;
import lombok.Setter;

@Getter
@EzyPrototype
public class ClassA {

	private final ClassB classB;
	private final ClassD classD;
	
	@EzyAutoBind
	@Setter
	private ClassC classC;
	
	@EzyAutoBind
	public ClassE classE;
	
	@EzyAutoBind("classB")
	public ClassA(ClassB classB, ClassD classD) {
		this.classB = classB;
		this.classD = classD;
	}
	
	@EzyPostInit
	public void haha() {
		System.out.println("hello world");
	}
	
}
