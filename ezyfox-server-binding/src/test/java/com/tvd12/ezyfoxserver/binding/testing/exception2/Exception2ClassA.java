package com.tvd12.ezyfoxserver.binding.testing.exception2;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyObjectBinding
public class Exception2ClassA {

	@SuppressWarnings("rawtypes")
	private Map map = new HashMap<>();
	
	
}
