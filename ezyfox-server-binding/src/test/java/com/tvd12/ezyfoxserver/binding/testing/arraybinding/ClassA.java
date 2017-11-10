package com.tvd12.ezyfoxserver.binding.testing.arraybinding;

import java.util.Map;

import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EzyArrayBinding
public class ClassA {

	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private String ignore1;
	
	@SuppressWarnings("rawtypes")
	private Map ignore2;
	
	private String null1;
	
	private String a = "1";
	private String b = "2";
	private String c = "3";
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	public String d = "4";
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	public String e = "5";
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	public String f = "6";
	
}
