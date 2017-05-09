package com.tvd12.ezyfoxserver.binding.testing.arraybinding;

import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;

@EzyArrayBinding(accessType = EzyAccessType.DECLARED_ELEMENTS)
public class ClassA7 extends ClassA6 {

	protected String a61 = "a61";
	protected String a62 = "a62";
	public String a63 = "a63";
	protected List<?> a64 = Lists.newArrayList(1, 2, 3);

	public String getA61() {
		return a61;
	}

	public void setA61(String a61) {
		this.a61 = a61;
	}

	public List<?> getA64() {
		return a64;
	}

	public void setA64(List<?> a64) {
		this.a64 = a64;
	}
	
}
