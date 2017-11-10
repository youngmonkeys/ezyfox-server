package com.tvd12.ezyfoxserver.binding.testing.testing1;

import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;

import lombok.Getter;
import lombok.Setter;

@EzyArrayBinding
public class ClassC implements InterfaceC {

	@Setter
	@Getter
	private String name;
	
}
