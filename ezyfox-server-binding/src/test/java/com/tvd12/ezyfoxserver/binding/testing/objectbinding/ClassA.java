package com.tvd12.ezyfoxserver.binding.testing.objectbinding;

import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyValue;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyObjectBinding(accessType = EzyAccessType.NONE)
public class ClassA {
	
	@EzyValue("1")
	private String a = "1";

	@EzyValue("2")
	private String b = "2";
	
	@EzyValue("3")
	private List<?> c = Lists.newArrayList(1, 2, 3);
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@EzyValue("4")
	private String d = "4";
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@EzyValue("5")
	private String e = "5";
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@EzyValue("9")
	public List<?> i = Lists.newArrayList(1, 2, 3);
	
	@EzyValue("6")
	public void getF() {
	}
	
	@EzyValue("6")
	public void setF(String a, String b) {
		
	}
	
	@EzyValue("7")
	protected String getG() {
		return "g";
	}
	
	@EzyValue("8")
	public List<?> getH() {
		return Lists.newArrayList(1, 2, 3);
	}
	
	@EzyValue("8")
	public void setH(List<?> h) {
		
	}
}
