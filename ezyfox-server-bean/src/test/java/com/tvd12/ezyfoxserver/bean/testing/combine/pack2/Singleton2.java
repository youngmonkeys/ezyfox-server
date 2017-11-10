package com.tvd12.ezyfoxserver.bean.testing.combine.pack2;

import java.util.ArrayList;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Getter;
import lombok.Setter;

@Getter
@EzySingleton("s2")
public class Singleton2 {

	@Setter
	@EzyAutoBind
	private ArrayList<String> list;
	
	@Setter
	@EzyAutoBind
	private ISingleton10 sgt10;
	
}
