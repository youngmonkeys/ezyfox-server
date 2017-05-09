package com.tvd12.ezyfoxserver.binding.testing.arraybinding;

import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyIndex;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EzyArrayBinding(indexes = {"a", "d", "z1", "f", "g", "h", "z2", "i"}, accessType = EzyAccessType.NONE)
public class ClassA4 {

	@EzyIndex(0)
	private String a = "1";
	
	@EzyIndex(1)
	private String b = "2";
	
	@EzyIndex(2)
	private String c = "3";
	
	private String d = "4";
	
	@EzyIndex(5)
	private String f =  "5";
	
	@EzyIndex(6)
	private List<Integer> g = Lists.newArrayList(1, 2, 3, 4, 5);
	
	@EzyIndex(7)
	private List<?> h = Lists.newArrayList(1, 2, 3, 4, 5);
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@EzyIndex(8)
	public List<?> i = Lists.newArrayList(1, 2, 3);
	
	@EzyIndex(9)
	public void setJ(String a, String b) {
	}
	
	@EzyIndex(9)
	public void getJ() {
	}
	
	@EzyIndex(10)
	public void setK(String k) {
		
	}
	
	@EzyIndex(11)
	public String getK() {
		return "k";
	}
	
}
