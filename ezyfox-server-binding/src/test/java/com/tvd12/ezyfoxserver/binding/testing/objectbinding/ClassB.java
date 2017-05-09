package com.tvd12.ezyfoxserver.binding.testing.objectbinding;

import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.annotation.EzyIgnore;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;

@EzyObjectBinding(accessType = EzyAccessType.ALL)
public class ClassB {

	private List<?> a = Lists.newArrayList(1, 2, 3);

	public List<?> getA() {
		return a;
	}

	public void setA(List<?> a) {
		this.a = a;
	}
	
	public List<?> getB() {
		return Lists.newArrayList(1, 2, 3);
	}
	
	public void setB(List<?> b) {
		
	}
	
	@EzyIgnore
	public void setC(String c) {
		
	}
	
	@EzyIgnore
	public String getC() {
		return "c";
	}
	
	protected void setD(String d) {
	}
	
	protected String getD() {
		return "d";
	}
	
}
