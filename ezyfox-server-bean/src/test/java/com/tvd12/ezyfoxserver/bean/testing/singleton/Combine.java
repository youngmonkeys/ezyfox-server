package com.tvd12.ezyfoxserver.bean.testing.singleton;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Getter;
import lombok.Setter;

@EzySingleton
public class Combine {

	@Setter
	@Getter
	private ClassA classA;
	
	@Getter
	private ClassA classA1;
	
	@Setter
	@Getter
	private ClassB classB;
	
	@Getter
	private ClassB classB1;
	
	@Setter
	@Getter
	@EzyAutoBind
	private ClassC classC;
	
	@Setter
	@Getter
	@EzyAutoBind("xa")
	private XClassA xClassA;
	
	@EzyAutoBind
	public XClassB xClassB;
	
	@EzyAutoBind
	protected XClassB xClassB1;
	
	public XClassB xClassB2;
	
	@EzyAutoBind("xb")
	public XClassB xClassB3;
	
	@EzyAutoBind({"a", "b"})
	public Combine(ClassA classA, ClassB classB) {
		this.classA = classA;
		this.classB = classB;
	}
	
	@EzyAutoBind
	public void setXClassB(XClassB xClassB) {
		this.xClassB = xClassB;
	}
	
	@EzyAutoBind
	public void setXClassBB(XClassB xClassB) {
		this.xClassB = xClassB;
	}
	
	@EzyAutoBind
	public void setClassA1(ClassA clazzA1) {
		this.classA1 = clazzA1;
	}
	
	@EzyAutoBind("b")
	public void setClassB1(ClassB clazzB1) {
		this.classB1 = clazzB1;
	}
	
	@EzyPostInit
	public void hello() {
		System.out.println("hung dep trai");
	}
	
	@EzyPostInit
	public void hello1(String value) {
		System.out.println("hung dep trai");
	}
	
}
