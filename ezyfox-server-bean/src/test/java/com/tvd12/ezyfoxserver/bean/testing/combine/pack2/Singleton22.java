package com.tvd12.ezyfoxserver.bean.testing.combine.pack2;

import java.util.ArrayList;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Getter;
import lombok.Setter;

@Getter
@EzySingleton("s22")
public class Singleton22 {

	@Setter
	@EzyAutoBind
	private ArrayList<String> list;
	
	@EzyAutoBind({"singleton21", "abc"})
	public Singleton22(ISingleton21 singleton21) {
		
	}
	
//	@EzyAutoBind({"singleton21", "abc"})
	public Singleton22(ISingleton21 singleton21, ISingleton10 singleton10, ISingleton22 singleton22) {
		
	}
	
}
