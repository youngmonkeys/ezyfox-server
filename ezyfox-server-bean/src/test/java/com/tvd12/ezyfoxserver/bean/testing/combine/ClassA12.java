package com.tvd12.ezyfoxserver.bean.testing.combine;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.testing.combine.pack1.ClassA1;
import com.tvd12.ezyfoxserver.bean.testing.combine.pack2.ClassA2;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassA12 {
	
	@EzyAutoBind
	private Singleton12 singleton12;
	
	@EzyAutoBind("a1")
	private ClassA1 classA1;

	@EzyAutoBind("a2")
	private ClassA2 classA2;
	
}
