package com.tvd12.ezyfoxserver.binding.testing.objectbinding;

import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyObjectBinding(accessType = EzyAccessType.DECLARED_FIELDS)
public class ClassD extends ClassA {

	private String a1 = "1";
	private String b1 = "2";
	private String c1 = "3";
	
	public void setD1(String d1) {
	}
	
	public String getD1() {
		return "d1";
	}
	
}
